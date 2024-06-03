package com.rparnp.gist_tool.exceptions;

public class DatabaseConnectionException extends RuntimeException {

    private static final String MESSAGE = "An internal database connection exception has occurred.";

    public DatabaseConnectionException() {
        super(MESSAGE);
    }
}
