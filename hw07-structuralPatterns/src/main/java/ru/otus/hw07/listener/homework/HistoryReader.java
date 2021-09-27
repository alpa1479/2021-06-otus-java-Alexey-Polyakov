package ru.otus.hw07.listener.homework;

import ru.otus.hw07.model.Message;

import java.util.Optional;

public interface HistoryReader {

    Optional<Message> findMessageById(long id);
}
