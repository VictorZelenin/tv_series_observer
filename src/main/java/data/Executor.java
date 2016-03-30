package data;

import org.hibernate.Session;

/**
 *
 */
public interface Executor<T> {
    T execute(Session session);
}
