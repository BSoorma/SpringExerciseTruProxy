package com.example.exceptions;

public class CustomServerException extends RuntimeException {

    private final int httpStatus;

    public CustomServerException(int httpStatus) {
        super("5xx server error calling downstream");
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

}
