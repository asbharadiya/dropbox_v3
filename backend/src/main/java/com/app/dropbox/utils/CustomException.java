package com.app.dropbox.utils;

public class CustomException extends Exception {

    private int code;

    // Parameterless Constructor
    public CustomException() {}

    // Constructor that accepts a message
    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
