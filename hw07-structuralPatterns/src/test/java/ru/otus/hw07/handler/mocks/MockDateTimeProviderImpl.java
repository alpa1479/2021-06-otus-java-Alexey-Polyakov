package ru.otus.hw07.handler.mocks;

import ru.otus.hw07.processor.homework.DateTimeProvider;

import java.time.LocalDateTime;

public class MockDateTimeProviderImpl implements DateTimeProvider {

    @Override
    public LocalDateTime getDate() {
        return LocalDateTime.of(2021, 1, 1, 1, 1, 2);
    }
}
