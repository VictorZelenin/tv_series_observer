package logging;

/**
 * Логирование нагрузки сервера
 */
public class ServerLogging extends Thread{

    public ServerLogging()
    {
        //запускается каждую минуту
        //логируется количество подключений к серверу приложений
    }

    @Override
    public void run() {
        super.run();
    }
}
