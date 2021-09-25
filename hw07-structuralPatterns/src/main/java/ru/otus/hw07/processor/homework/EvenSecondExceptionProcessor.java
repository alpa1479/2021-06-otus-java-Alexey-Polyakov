package ru.otus.hw07.processor.homework;

import ru.otus.hw07.model.Message;
import ru.otus.hw07.processor.Processor;

import java.time.Instant;

public class EvenSecondExceptionProcessor implements Processor {

    @Override
    public Message process(Message message) {
        final long currentSecond = Instant.now().getEpochSecond();
        if (currentSecond % 2 == 0) {
            throw new EvenSecondException(String.format("Current second is even - %d", currentSecond));
        }
        return message;
    }
}
