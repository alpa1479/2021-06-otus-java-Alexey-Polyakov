package ru.otus.hw07.listener.homework;

import ru.otus.hw07.listener.Listener;
import ru.otus.hw07.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> messages = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        final Message copy = msg.copy();
        messages.put(copy.getId(), copy);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messages.get(id));
    }
}
