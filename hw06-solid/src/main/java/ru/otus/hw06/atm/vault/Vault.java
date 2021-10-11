package ru.otus.hw06.atm.vault;

import ru.otus.hw06.banknote.Banknote;

import java.util.List;

public interface Vault {

    List<Banknote> get(int amount);

    void put(List<Banknote> banknotes);

    int calculateBalance();
}
