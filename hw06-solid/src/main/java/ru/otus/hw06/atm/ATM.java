package ru.otus.hw06.atm;

import ru.otus.hw06.banknote.list.BanknoteList;

public interface ATM {

    void deposit(BanknoteList banknoteList);

    BanknoteList withdraw(int amount);

    int getBalance();
}
