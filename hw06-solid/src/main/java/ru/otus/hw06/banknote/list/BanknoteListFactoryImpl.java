package ru.otus.hw06.banknote.list;

import ru.otus.hw06.banknote.Banknote;

import java.util.List;

public class BanknoteListFactoryImpl implements BanknoteListFactory {

    @Override
    public BanknoteList from(Banknote... banknotes) {
        return new BanknoteListImpl(banknotes);
    }

    @Override
    public BanknoteList from(List<Banknote> banknotes) {
        return new BanknoteListImpl(banknotes);
    }
}
