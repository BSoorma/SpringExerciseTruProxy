package com.example.exceptions;

public class CustomClientException extends RuntimeException {

    private final int httpStatus;

    public CustomClientException(int httpStatus) {
        super("4xx client error calling downstream");
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

}
