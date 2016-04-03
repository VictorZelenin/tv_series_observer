package logging;

/**
 * Логирование нагрузки базы данных
 */
public class DataBaseLogging extends Thread {

    public DataBaseLogging()
    {
        //запускается каждую минуту
        //тестируется количество коннектов к базе и выводит в лог-файл
    }

    @Override
    public void run() {

    }
}
