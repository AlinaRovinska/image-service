package com.project.imageservice.exception.type;

public class AccountNotFoundException extends EntityNotFoundException {

    public AccountNotFoundException(Integer accountId) {
        super(String.format("Account with id '%s' not found", accountId));
    }
}
