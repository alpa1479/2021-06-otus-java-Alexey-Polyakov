package ru.otus.hw07.processor.homework;

import ru.otus.hw07.model.Message;
import ru.otus.hw07.processor.Processor;

public class EvenSecondExceptionProcessor implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public EvenSecondExceptionProcessor(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        final long currentSecond = dateTimeProvider.getDate().getSecond();
        if (currentSecond % 2 == 0) {
            throw new EvenSecondException(String.format("Current second is even - %d", currentSecond));
        }
        return message;
    }
}
