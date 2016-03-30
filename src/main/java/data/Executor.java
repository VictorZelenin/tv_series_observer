package data;

import org.hibernate.Session;

/**
 * Функция для выполнения запроса с помощью Hibernate
 */
@FunctionalInterface
public interface Executor<T> {
    T execute(Session session);
}
