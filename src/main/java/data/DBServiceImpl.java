package data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 *
 */
public class DBServiceImpl implements DBService{
    private final SessionFactory sessionFactory;
    private static DBService dbService;

    private DBServiceImpl()
    {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(ssrb.build());
    }

    public synchronized DBService getInstance()
    {
        if (dbService == null)
        {
            dbService = new DBServiceImpl();
        }
        return dbService;
    }

    @Override
    public <T> T execute(Executor<T> executor) {
        Session session = sessionFactory.openSession();
        T result = executor.execute(session);
        session.close();
        return result;
    }


}
