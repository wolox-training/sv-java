package com.wolox.training.exceptions;

public class BookRepeatedTitleException extends RuntimeException{

    public BookRepeatedTitleException() {
    }

    public BookRepeatedTitleException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookRepeatedTitleException(String message) {
        super(message);
    }

    public BookRepeatedTitleException(Throwable cause) {
        super(cause);
    }
}
