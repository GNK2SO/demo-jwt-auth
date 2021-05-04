package com.gnk2so.auth.user.exception;

public class AlreadyUsedEmailException extends RuntimeException {

    public static final String MESSAGE = "Already used email";

    public AlreadyUsedEmailException() {
        super(MESSAGE);
    }

}
