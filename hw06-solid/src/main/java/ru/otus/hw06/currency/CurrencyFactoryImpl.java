package ru.otus.hw06.currency;

import ru.otus.hw06.banknote.Banknote;

import java.util.List;

public class CurrencyFactoryImpl implements CurrencyFactory {

    @Override
    public Currency from(Banknote... banknotes) {
        return new CurrencyImpl(banknotes);
    }

    @Override
    public Currency from(List<Banknote> banknotes) {
        return new CurrencyImpl(banknotes);
    }
}
