package ru.otus.hw07.handler;

import ru.otus.hw07.listener.Listener;
import ru.otus.hw07.model.Message;

public interface Handler {

    Message handle(Message msg);

    void addListener(Listener listener);

    void removeListener(Listener listener);
}
