package ru.otus.hw06.currency;

import ru.otus.hw06.banknote.Banknote;

import java.util.List;

public interface Currency {

    int getTotalAmount();

    List<Banknote> getBanknotes();
}
