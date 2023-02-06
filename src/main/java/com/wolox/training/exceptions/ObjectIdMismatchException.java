package com.wolox.training.exceptions;

public class ObjectIdMismatchException extends RuntimeException{

    public ObjectIdMismatchException() {
    }

    public ObjectIdMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectIdMismatchException(String message) {
        super(message);
    }

    public ObjectIdMismatchException(Throwable cause) {
        super(cause);
    }
}
