package data;

/**
 *
 */
public interface DBService {

    DBService getInstance();

    <T> T execute(Executor<T> executor);
}
