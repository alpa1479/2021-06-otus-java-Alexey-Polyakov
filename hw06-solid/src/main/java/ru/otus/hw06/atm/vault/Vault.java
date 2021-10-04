package ru.otus.hw06.atm.vault;

import ru.otus.hw06.banknote.Banknote;

import java.util.List;
import java.util.Optional;

public interface Vault {

    Optional<List<Banknote>> get(int amount);

    void put(List<Banknote> banknotes);

    int calculateBalance();
}
