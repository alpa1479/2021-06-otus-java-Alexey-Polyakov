package ru.otus.hw06.banknote;

public enum UnknownBanknote implements Banknote {
    ONE(1),
    TWO(2),
    FIVE(5),
    TEN(10),
    TWENTY(20),
    FIFTY(50),
    ONE_HUNDRED(100);

    private final int value;

    UnknownBanknote(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
