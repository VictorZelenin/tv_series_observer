package data.dao;

import org.hibernate.Session;

import java.util.List;

/**
 * Базовый интерфейс дао-класса
 */
abstract class DAO<T> {
    protected Session currentSession;

    public DAO(Session session)
    {
        this.currentSession = session;
    }

    public abstract T get(long id);

    public abstract List<T> getAll();

    public abstract void save(T obj);

    public abstract void delete(T obj);

    public abstract void deleteAll();

    public Session getCurrentSession()
    {
        return currentSession;
    }
}
