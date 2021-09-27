package ru.otus.hw07.processor.homework;

import java.time.LocalDateTime;

public class DateTimeProviderImpl implements DateTimeProvider {

    @Override
    public LocalDateTime getDate() {
        return LocalDateTime.now();
    }
}
