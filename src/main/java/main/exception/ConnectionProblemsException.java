package main.exception;

public class ConnectionProblemsException extends Exception {
    public ConnectionProblemsException() {
        super();
    }

    public ConnectionProblemsException(String message) {
        super(message);
    }
}
