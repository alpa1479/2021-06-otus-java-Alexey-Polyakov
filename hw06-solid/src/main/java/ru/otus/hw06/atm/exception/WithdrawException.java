package ru.otus.hw06.atm.exception;

public class WithdrawException extends ATMOperationException {

    public WithdrawException(String message) {
        super(message);
    }
}
