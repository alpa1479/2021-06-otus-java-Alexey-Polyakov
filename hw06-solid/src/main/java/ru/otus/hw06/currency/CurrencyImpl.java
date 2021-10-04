package ru.otus.hw06.currency;

import ru.otus.hw06.banknote.Banknote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class CurrencyImpl implements Currency {

    private final List<Banknote> banknotes;

    CurrencyImpl(Banknote... banknotes) {
        this.banknotes = new ArrayList<>(Arrays.asList(banknotes));
    }

    CurrencyImpl(List<Banknote> banknotes) {
        this.banknotes = new ArrayList<>(banknotes);
    }

    @Override
    public int getTotalAmount() {
        return banknotes.stream().mapToInt(Banknote::getValue).sum();
    }

    @Override
    public List<Banknote> getBanknotes() {
        return new ArrayList<>(banknotes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CurrencyImpl currency = (CurrencyImpl) o;
        return Objects.equals(banknotes, currency.banknotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(banknotes);
    }

    @Override
    public String toString() {
        return "UnknownCurrency{" +
                "banknotes=" + banknotes +
                '}';
    }
}
