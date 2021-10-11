package ru.otus.hw06.banknote;

import ru.otus.hw06.denomination.Denomination;

import java.util.Objects;

public class Banknote {

    private final Denomination denomination;

    private Banknote(Denomination denomination) {
        this.denomination = denomination;
    }

    public static Banknote with(Denomination denomination) {
        return new Banknote(denomination);
    }

    public int getDenominationValue() {
        return denomination.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Banknote banknote = (Banknote) o;
        return denomination == banknote.denomination;
    }

    @Override
    public int hashCode() {
        return Objects.hash(denomination);
    }

    @Override
    public String toString() {
        return "Banknote{" +
                "denomination=" + denomination +
                '}';
    }
}
