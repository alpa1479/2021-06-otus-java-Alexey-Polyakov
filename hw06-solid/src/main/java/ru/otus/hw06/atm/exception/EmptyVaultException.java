package ru.otus.hw06.atm.exception;

public class EmptyVaultException extends ATMOperationException {

    public EmptyVaultException(String message) {
        super(message);
    }
}
