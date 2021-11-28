package ru.otus.hw16.handlers;

import ru.otus.hw16.model.message.MessageType;

public interface HandlersStore {

    MessageHandler getHandlerByType(MessageType type);

    void addHandler(MessageType type, MessageHandler handler);
}
