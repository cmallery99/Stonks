package main.java.stonkexceptions;

public class NotEnoughSharesException extends Exception{
    public NotEnoughSharesException(String errorMessage) {
        super(errorMessage);
    }
}
