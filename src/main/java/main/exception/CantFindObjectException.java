package main.exception;


public class CantFindObjectException extends Exception{

    public CantFindObjectException() {
        super();
    }
    public CantFindObjectException(String message) {
        super(message);
    }
}
