package ru.otus.hw06.atm;

import ru.otus.hw06.currency.Currency;

public interface ATM {

    void deposit(Currency currency);

    Currency withdraw(int amount);

    int getBalance();
}
