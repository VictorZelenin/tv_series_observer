package data;

import data.dao.CountryDAO;
import data.dao.EpisodeTranslationDAO;
import data.dao.TVSeriesDAO;
import data.datasets.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.type.StandardBasicTypes;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Основной класс для работы с БД
 * Предпочтительно использовать его методы вместо методов DAO-класса, если есть возможность
 */
public class DBService implements  AutoCloseable{
    private final SessionFactory sessionFactory;
    private static DBService dbService;
    private static SimpleDateFormat toSqlDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


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


    public static SimpleDateFormat getToSqlDate() {
        return toSqlDate;
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
                .createSQLQuery("select * from episode_translation order by date desc limit " + n + ";")
                .addEntity(EpisodeTranslation.class)
                .list();
    }


    //получение записей из таблицы episode_translation в промежутке с fromDate по toDate
    //по определённому сериалу/сезону/серии (если что-то не выбрано, передавать null)  + озвучке
    public List<EpisodeTranslation> getEpisodesTranslations(Session session, Date fromDate, Date toDate, String tvSeriesName, Integer seasonNumber, Integer episodeNumber, String translationType)
    {
        StringBuilder query = new StringBuilder("SELECT episode_translation.* FROM episode_translation ");
        List<String> queryConditions = new LinkedList<>();

        if (translationType != null  && translationType != "")
        {
            query.append(" JOIN translation ON episode_translation.translation_id = translation.id ");
            queryConditions.add(" translation.type = '" + translationType + "' ");
        }

        if (fromDate != null)
        {
            queryConditions.add(" episode_translation.date >= '" + getToSqlDate().format(fromDate) + "' ");
        }
        if (toDate != null)
        {
            queryConditions.add(" episode_translation.date <= '" + getToSqlDate().format(toDate) + "' ");
        }

        if (tvSeriesName != null)
        {
            query.append(" JOIN tv_episode ON episode_translation.episode_id = tv_episode.id " +
                    "JOIN season ON tv_episode.season_id = season.id " +
                    "JOIN tv_series ON season.tv_series_id = tv_series.id ");
            queryConditions.add(" tv_series.name = '" + tvSeriesName + "' ");

            if (seasonNumber != null)
            {
                queryConditions.add(" season.season_number = " + seasonNumber + " ");

                if (episodeNumber != null)
                    queryConditions.add(" tv_episode.episode_number = " + episodeNumber + " ");
            }
        }


        if (!queryConditions.isEmpty())
        {
            query.append(" WHERE ");
            for (int i = 0; i < queryConditions.size() - 1; i++)
            {
                query.append(queryConditions.get(i));
                query.append(" AND ");
            }

            query.append(queryConditions.get(queryConditions.size() - 1));
            query.append(" ");
        }
        query.append(";");



        return session.createSQLQuery(query.toString())
                .addEntity(EpisodeTranslation.class)
                .list();
    }


    //получение сериалов по параметрам
    public List<TVSeries> getTVSeries(Session session, TVSeriesSearchParameters tvSeriesSearchParameters)
    {
        StringBuilder query = new StringBuilder("SELECT tv_series.* FROM tv_series ");
        List<String> queryConditions = new LinkedList<>();

        if (tvSeriesSearchParameters != null)
        {
            if (tvSeriesSearchParameters.getTranslations().size() > 0)
            {
                query.append(" JOIN season ON tv_series.id = season.tv_series_id " +
                        "JOIN tv_episode ON season.id = tv_episode.season_id " +
                        "JOIN episode_translation ON tv_episode.id = episode_translation.tv_episode_id ");

                StringBuilder translations = new StringBuilder();
                List<Translation> translationsList = new ArrayList<>(tvSeriesSearchParameters.getTranslations());
                for (int i = 0; i < translationsList.size() - 1; i++)
                    translations.append(translationsList.get(i).getId() + ", ");
                translations.append(translationsList.get(translationsList.size() - 1).getId());

                queryConditions.add(" episode_translation.translation_id IN (" + translations + ") ");
            }

            if (tvSeriesSearchParameters.getInCountries().size() > 0)
            {
                List<Country> countriesList = new ArrayList<>(tvSeriesSearchParameters.getInCountries());
                StringBuilder countries = new StringBuilder();
                for (int i = 0; i < countriesList.size() - 1; i++)
                    countries.append(countriesList.get(i).getId() + ", ");
                countries.append(countriesList.get(countriesList.size() - 1).getId());

                queryConditions.add(" tv_series.country_id IN (" + countries + ") ");
            }

            if (tvSeriesSearchParameters.getNotInCountries().size() > 0)
            {
                List<Country> countriesList = new ArrayList<>(tvSeriesSearchParameters.getNotInCountries());
                StringBuilder countries = new StringBuilder();
                for (int i = 0; i < countriesList.size() - 1; i++)
                    countries.append(countriesList.get(i).getId() + ", ");
                countries.append(countriesList.get(countriesList.size() - 1).getId());

                queryConditions.add(" tv_series.country_id NOT IN (" + countries + ") ");
            }

            if (tvSeriesSearchParameters.getMinRating() != null)
            {
                queryConditions.add(" tv_series.imdb >= " + tvSeriesSearchParameters.getMinRating() + " ");
            }

            if (tvSeriesSearchParameters.getMinDate() != null)
            {
                queryConditions.add(" tv_series.begin_date >= " + getToSqlDate().format(tvSeriesSearchParameters.getMinDate()) + " ");
            }

            if (tvSeriesSearchParameters.getMaxDate() != null)
            {
                queryConditions.add(" tv_series.begin_date < " + getToSqlDate().format(tvSeriesSearchParameters.getMaxDate()) + " ");
            }
        }

        if (!queryConditions.isEmpty())
        {
            query.append("  WHERE ");
            for (int i = 0; i < queryConditions.size() - 1; i++)
            {
                query.append(queryConditions.get(i));
                query.append(" AND ");
            }

            query.append(queryConditions.get(queryConditions.size() - 1));
            query.append(" ");

        }

        if (tvSeriesSearchParameters.getSortBy() != null)
        {
            query.append(" ORDER BY ");
            switch (tvSeriesSearchParameters.getSortBy())
            {
                case NAME :
                    query.append(" tv_series.name ");
                    break;
                case RATING :
                    query.append(" tv_series.imdb ");
                    break;
                case DATE :
                    query.append(" tv_series.begin_date ");
                    break;
            }

            if (!tvSeriesSearchParameters.isAscendingOrder())
                query.append(" DESC ");

        }

        query.append(";");

        return session.createSQLQuery(query.toString())
                .addEntity(TVSeries.class)
                .list();
    }

    //параметры для запроса getTVSeries()
    public static class TVSeriesSearchParameters
    {
        private Set<Country> inCountries = new HashSet<>();
        private Set<Country> notInCountries = new HashSet<>();

        private Set<Translation> translations = new HashSet<>();

        private Date minDate;
        private Date maxDate;

        private Float minRating;

        private SortBy sortBy;
        private boolean ascendingOrder = false; // true — в порядке возрастания, false — в порядке убывания

        public static enum SortBy
        {
            RATING,
            DATE,
            NAME
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

        public boolean isAscendingOrder() {
            return ascendingOrder;
        }

        public void setAscendingOrder(boolean ascendingOrder) {
            this.ascendingOrder = ascendingOrder;
        }
    }

    //Количество сериалов по странам
    public Map<Country, Integer> getTVSeriesAmountPerCountry(Session session)
    {
        Map<Country, Integer> resultMap = new HashMap<>();

        List<Object[]> resultList = session.createSQLQuery("SELECT country.*,  COUNT(tv_series.id) AS tv_series_quantity" +
                " FROM country LEFT JOIN tv_series ON country.id = tv_series.country_id" +
                " GROUP BY country.id;")
                .addEntity("country", Country.class)
                .addScalar("tv_series_quantity", StandardBasicTypes.INTEGER)
                .list();

        for (Object[] row: resultList)
        {
            resultMap.put((Country)row[0], (Integer)row[1]);
        }
        return resultMap;
    }

}
