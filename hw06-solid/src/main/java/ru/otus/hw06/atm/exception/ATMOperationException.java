package ru.otus.hw06.atm.exception;

public abstract class ATMOperationException extends RuntimeException {

    public ATMOperationException(String message) {
        super(message);
    }
}
