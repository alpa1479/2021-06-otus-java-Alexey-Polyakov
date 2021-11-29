package ru.otus.hw16.handlers;

import ru.otus.hw16.model.message.Message;
import ru.otus.hw16.model.resultdatatype.ResultDataType;

import java.util.Optional;

public interface MessageHandler {

    <T extends ResultDataType> Optional<Message<T>> handle(Message<T> msg);
}
