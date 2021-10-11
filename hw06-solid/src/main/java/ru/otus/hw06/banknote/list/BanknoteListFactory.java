package ru.otus.hw06.banknote.list;

import ru.otus.hw06.banknote.Banknote;

import java.util.List;

public interface BanknoteListFactory {

    BanknoteList from(Banknote... banknotes);

    BanknoteList from(List<Banknote> banknotes);
}
