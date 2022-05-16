package com.project.imageservice.exception.type;

public class AccountAlreadyExistException extends RuntimeException {

    public AccountAlreadyExistException(String userName) {
        super(String.format("Account with username %s already exists", userName));
    }
}
