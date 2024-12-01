package com.example.exceptions;

public class CustomServerException extends RuntimeException {

    public CustomServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
