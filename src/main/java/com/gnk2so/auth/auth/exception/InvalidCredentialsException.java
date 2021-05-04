package com.gnk2so.auth.auth.exception;

public class InvalidCredentialsException extends RuntimeException {
    
    public static final String MESSAGE = "Invalid credentials!";
    
    public InvalidCredentialsException() {
        super(MESSAGE);
    }

}
