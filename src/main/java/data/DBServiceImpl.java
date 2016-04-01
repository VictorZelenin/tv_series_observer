package data;

import data.datasets.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Реализация DBService
 */
public class DBServiceImpl implements DBService, AutoCloseable{
    private final SessionFactory sessionFactory;
    private static DBServiceImpl dbService;

    private DBServiceImpl()
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

    public static synchronized DBServiceImpl getInstance()
    {
        if (dbService == null) {
            dbService = new DBServiceImpl();
        }
        return dbService;
    }


//    Выполняет переданную функцию
//    (Открывает и закрывает сессию)
    @Override
    public <T> T execute(Executor<T> executor) {
        try(Session session = sessionFactory.openSession()) {
            return executor.execute(session);
        }
    }



    @Override
    public void close()
    {
        sessionFactory.close();
        dbService = null;
    }


    //для ручной работы с сессией
    //обязательно вызывать метод close после работы с объектом
    public Session getSession()
    {
        synchronized (dbService)
        {
            return sessionFactory.openSession();
        }
    }


}
