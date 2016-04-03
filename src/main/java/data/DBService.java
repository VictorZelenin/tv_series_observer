package data;

import data.dao.CountryDAO;
import data.dao.EpisodeTranslationDAO;
import data.dao.TVSeriesDAO;
import data.datasets.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.*;

/**
 * Основной класс для работы с БД
 * Предпочтительно использовать его методы вместо методов DAO-класса, если есть возможность
 */
public class DBService implements  AutoCloseable{
    private final SessionFactory sessionFactory;
    private static DBService dbService;

    private DBService()
    {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Country.class);
        configuration.addAnnotatedClass(TVSeries.class);
        configuration.addAnnotatedClass(Season.class);
        configuration.addAnnotatedClass(Episode.class);
        configuration.addAnnotatedClass(Translation.class);
        configuration.addAnnotatedClass(EpisodeTranslation.class);
        configuration.configure("hibernate.cfg.xml");
        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(ssrb.build());
    }

    public static synchronized DBService getInstance()
    {
        if (dbService == null) {
            dbService = new DBService();
        }
        return dbService;
    }


//    Выполняет переданную функцию
//    (Открывает и закрывает сессию)
    public <T> T execute(Executor<T> executor) {
        try(Session session = sessionFactory.openSession()) {
            return executor.execute(session);
        }
    }



    public void close()
    {
        sessionFactory.close();
        dbService = null;
    }


    //для ручной работы с сессией
    //обязательно вызывать метод close после работы с объектом
    public Session getSession()
    {
        synchronized (sessionFactory)
        {
            return sessionFactory.openSession();
        }
    }


    //сохраняет все изменения для объектов, вызванных данной сессией
    public void saveAllChanges(Session session)
    {
        session.flush();
    }


    //получение последних по дате n записей из таблицы episode_translation
    public List<EpisodeTranslation> getLastEpisodesTranslations(Session session, int n)
    {
        return session
                .createSQLQuery("select * from country order by date desc")
                .addEntity(EpisodeTranslation.class)
                .list();
    }


    //получение записей из таблицы episode_translation в промежутке с fromDate по toDate
    //по определённому сериалу/сезону/серии  + озвучке
    public List<EpisodeTranslation> getEpisodesTranslations(Session session, Date fromDate, Date toDate, TVSeries tvSeries, Season season, Episode episode, Translation translation)
    {
        if (fromDate == null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(new Date());
            calendar.add(Calendar.YEAR, -1000);
            fromDate = calendar.getTime();
        }

        if (toDate == null)
        {
            toDate = new Date();
        }

        List<EpisodeTranslation> allRecordsList = new EpisodeTranslationDAO(session).getAll();
        List<EpisodeTranslation> resultList = new LinkedList<>();
        boolean saveIt = true;
        for (EpisodeTranslation episodeTranslation: resultList)
        {
            if (!(episodeTranslation.getDate().compareTo(fromDate) >= 0 && episodeTranslation.getDate().compareTo(toDate) < 0))
            {
                saveIt = false;
                continue;
            }
            if (episode != null && episode.getId() != episodeTranslation.getEpisode().getId())
            {
                saveIt = false;
                continue;
            }
            if (translation != null && translation.getId() != episodeTranslation.getTranslation().getId())
            {
                saveIt = false;
                continue;
            }
            if (season != null && season.getId() != episodeTranslation.getSeason().getId())
            {
                saveIt = false;
                continue;
            }
            if (tvSeries != null && tvSeries.getId() != episodeTranslation.getTVSeries().getId())
            {
                saveIt = false;
                continue;
            }

            if (saveIt)
                resultList.add(episodeTranslation);
        }

        return resultList;
    }


    //получение сериалов по параметрам
    public List<TVSeries> getTVSeries(Session session, TVSeriesSearchParameters tvSeriesSearchParameters)
    {
        List<EpisodeTranslation> episodeTranslationsList = new EpisodeTranslationDAO(session).getAll();
        Set<TVSeries> resultSet = new HashSet<>();

        boolean saveIt = true;
        for (EpisodeTranslation episodeTranslation : episodeTranslationsList)
        {
            if (!tvSeriesSearchParameters.getInCountries().contains(episodeTranslation.getCountry())) {
                saveIt = false;
                continue;
            }

            if (tvSeriesSearchParameters.getNotInCountries().contains(episodeTranslation.getCountry())) {
                saveIt = false;
                continue;
            }

            if (!tvSeriesSearchParameters.getTranslations().contains(episodeTranslation.getTranslation())) {
                saveIt = false;
                continue;
            }

            if (tvSeriesSearchParameters.getMinDate().compareTo(episodeTranslation.getDate()) > 0)
            {
                saveIt = false;
                continue;
            }

            if (tvSeriesSearchParameters.getMaxDate().compareTo(episodeTranslation.getDate()) < 0)
            {
                saveIt = false;
                continue;
            }

            if (tvSeriesSearchParameters.getMinRating().compareTo(episodeTranslation.getTVSeries().getRating()) > 0)
            {
                saveIt = false;
                continue;
            }

            if (saveIt)
                resultSet.add(episodeTranslation.getTVSeries());
        }

        List<TVSeries> resultList = new ArrayList<>(resultSet);

        //тут должна быть сортировка, но я всё равно буду переделывать на более оптимальное

        return resultList;
    }

    //параметры для запроса getTVSeries()
    public static class TVSeriesSearchParameters
    {
        private Set<Country> inCountries;
        private Set<Country> notInCountries;

        private Set<Translation> translations;

        private Date minDate;
        private Date maxDate;

        private Float minRating;

        private SortBy sortBy;
        private boolean sortOrder; // true — в порядке возрастания, false — в порядке убывания

        public enum SortBy
        {
            RATING,
            YEAR,
            NAME
        }

        public TVSeriesSearchParameters()
        {
            inCountries = new HashSet<>();
            notInCountries = new HashSet<>();

            translations = new HashSet<>();

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(new Date());
            calendar.add(Calendar.YEAR, -1000);
            minDate = calendar.getTime();
            maxDate = new Date();

            minRating = 0.0f;
            sortBy = SortBy.NAME;
            sortOrder = true;
        }

        public Set<Country> getInCountries() {
            return inCountries;
        }

        public void setInCountries(Set<Country> inCountries) {
            this.inCountries = inCountries;
        }

        public Set<Country> getNotInCountries() {
            return notInCountries;
        }

        public void setNotInCountries(Set<Country> notInCountries) {
            this.notInCountries = notInCountries;
        }

        public Set<Translation> getTranslations() {
            return translations;
        }

        public void setTranslations(Set<Translation> translations) {
            this.translations = translations;
        }

        public Date getMinDate() {
            return minDate;
        }

        public void setMinDate(Date minDate) {
            this.minDate = minDate;
        }

        public Float getMinRating() {
            return minRating;
        }

        public void setMinRating(Float minRating) {
            this.minRating = minRating;
        }

        public Date getMaxDate() {
            return maxDate;
        }

        public void setMaxDate(Date maxDate) {
            this.maxDate = maxDate;
        }

        public SortBy getSortBy() {
            return sortBy;
        }

        public void setSortBy(SortBy sortBy) {
            this.sortBy = sortBy;
        }

        public boolean isSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(boolean sortOrder) {
            this.sortOrder = sortOrder;
        }
    }


    //Количество сериалов по странам
    public Map<Country, Integer> getTVSeriesAmountPerCountry(Session session)
    {
        List<TVSeries> tvSeriesList = new TVSeriesDAO(session).getAll();
        List<Country> countries = new CountryDAO(session).getAll();

        Map<Country, Integer> resultMap = new HashMap<>();

        for (Country country : countries)
            resultMap.put(country, 0);

        for (TVSeries tvSeries : tvSeriesList)
        {
                resultMap.put(tvSeries.getCountry(), resultMap.get(tvSeries.getCountry()) + 1);
        }

        return resultMap;
    }




}
