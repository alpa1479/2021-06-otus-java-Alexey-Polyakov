package ru.otus.hw06.banknote.list;

import ru.otus.hw06.banknote.Banknote;

import java.util.List;

public interface BanknoteList {

    int getTotalAmount();

    List<Banknote> getBanknotes();
}
