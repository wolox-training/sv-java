package com.wolox.training.exceptions;

public class BookAlreadyOwnedException extends RuntimeException{

    public BookAlreadyOwnedException() {
    }

    public BookAlreadyOwnedException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookAlreadyOwnedException(String message) {
        super(message);
    }

    public BookAlreadyOwnedException(Throwable cause) {
        super(cause);
    }
}
