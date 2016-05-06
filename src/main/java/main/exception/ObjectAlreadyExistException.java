package main.exception;


public class ObjectAlreadyExistException extends Exception{
    public ObjectAlreadyExistException() {
        super();
    }

    public ObjectAlreadyExistException(String message) {
        super(message);
    }
}
