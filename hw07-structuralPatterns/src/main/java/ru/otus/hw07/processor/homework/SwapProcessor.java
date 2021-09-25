package ru.otus.hw07.processor.homework;

import ru.otus.hw07.model.Message;
import ru.otus.hw07.processor.Processor;

public class SwapProcessor implements Processor {

    @Override
    public Message process(Message message) {
        final String field11 = message.getField11();
        final String field12 = message.getField12();
        return message.toBuilder().field11(field12).field12(field11).build();
    }
}
