package com.rparnp.gist_tool.exceptions;

public class NetworkException extends RuntimeException {

    private static final String MESSAGE = "An internal network exception has occured.";

    public NetworkException() {
        super(MESSAGE);
    }
}
