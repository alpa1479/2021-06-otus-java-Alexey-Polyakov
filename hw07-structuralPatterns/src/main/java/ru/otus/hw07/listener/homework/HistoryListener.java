package ru.otus.hw07.listener.homework;

import ru.otus.hw07.listener.Listener;
import ru.otus.hw07.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final List<Message> messages = new ArrayList<>();

    @Override
    public void onUpdated(Message msg) {
        final Message copy = msg.toBuilder().build();
        messages.add(copy);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return messages.stream().filter(msg -> msg.getId() == id).findFirst();
    }
}
