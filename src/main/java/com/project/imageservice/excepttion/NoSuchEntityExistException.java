package com.project.imageservice.excepttion;

public class NoSuchEntityExistException extends RuntimeException {


    public NoSuchEntityExistException(String message) {
        super(message);
    }
}
