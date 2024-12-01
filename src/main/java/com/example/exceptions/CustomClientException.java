package com.example.exceptions;

public class CustomClientException extends RuntimeException {

    public CustomClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
