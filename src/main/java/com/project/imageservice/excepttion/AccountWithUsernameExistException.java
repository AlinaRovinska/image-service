package com.project.imageservice.excepttion;

public class AccountWithUsernameExistException extends RuntimeException {

    public AccountWithUsernameExistException(String message) {
        super(message);
    }
}
