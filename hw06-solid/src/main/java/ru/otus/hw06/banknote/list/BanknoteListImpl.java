package ru.otus.hw06.banknote.list;

import ru.otus.hw06.banknote.Banknote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class BanknoteListImpl implements BanknoteList {

    private final List<Banknote> banknotes;

    BanknoteListImpl(Banknote... banknotes) {
        this.banknotes = new ArrayList<>(Arrays.asList(banknotes));
    }

    BanknoteListImpl(List<Banknote> banknotes) {
        this.banknotes = new ArrayList<>(banknotes);
    }

    @Override
    public int getTotalAmount() {
        return banknotes.stream().mapToInt(Banknote::getDenominationValue).sum();
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
        BanknoteListImpl currency = (BanknoteListImpl) o;
        return Objects.equals(banknotes, currency.banknotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(banknotes);
    }

    @Override
    public String toString() {
        return "BanknoteListImpl{" +
                "banknotes=" + banknotes +
                '}';
    }
}
