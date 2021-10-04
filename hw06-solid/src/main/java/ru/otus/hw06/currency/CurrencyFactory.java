package ru.otus.hw06.currency;

import ru.otus.hw06.banknote.Banknote;

import java.util.List;

public interface CurrencyFactory {

    Currency from(Banknote... banknotes);

    Currency from(List<Banknote> banknotes);
}
