package com.wolox.training.exceptions;

public class UserUsernameRepeatedException extends RuntimeException{

    public UserUsernameRepeatedException() {
    }

    public UserUsernameRepeatedException(String message) {
        super(message);
    }

    public UserUsernameRepeatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserUsernameRepeatedException(Throwable cause) {
        super(cause);
    }
}
