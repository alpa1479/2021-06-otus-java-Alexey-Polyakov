package ru.otus.hw17.model;

public record GeneratedNumber(int value) {

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
