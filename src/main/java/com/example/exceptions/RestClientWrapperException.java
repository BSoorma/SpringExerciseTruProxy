package com.example.exceptions;

public class RestClientWrapperException extends RuntimeException {

    public RestClientWrapperException(String message, Throwable cause) {
        super(message, cause);
    }
}
