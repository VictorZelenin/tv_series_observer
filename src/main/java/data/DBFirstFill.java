package data;

import org.hibernate.Session;

import java.io.*;

/**
 * Класс, отвечающий за первое заполнение базы данных
 * Для работы необходим интернет и созданная база данных с названием "TVSeriesObserverDB"
 * Сервер БД запускать на localhost:5431
 * Создайте пользователя root с паролем root
 * Или измените данные параметры в resources/hibernate.cfg.xml
 */
public class DBFirstFill {

    private static String pathToResourcesSQL = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "sql" + File.separator;

    public static void setTestData(Session session) throws IOException
    {
        File file = new File(pathToResourcesSQL + "fill_with_sample_data.sql");
        executeSQLFromFile(session, file);
    }

    public static void deleteAllData(Session session) throws IOException
    {
        File file = new File(pathToResourcesSQL + "delete_all.sql");
        executeSQLFromFile(session, file);
    }


    //используйте только после загрузки базовых данных
    public static void setIndexes(Session session) throws IOException
    {
        File file = new File(pathToResourcesSQL + "set_indexes.sql");
        executeSQLFromFile(session, file);
    }

    public static void dropIndexes(Session session) throws IOException
    {
        File file = new File(pathToResourcesSQL + "drop_indexes.sql");
        executeSQLFromFile(session, file);
    }

    public static void executeSQLFromFile(Session session, File file) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String query = "";
        while (reader.ready())
        {
            query += " " + reader.readLine();
        }
        query += "";
        session.createSQLQuery(query).executeUpdate();
    }

    public static String getPathToResourcesSQL() {
        return pathToResourcesSQL;
    }
}
