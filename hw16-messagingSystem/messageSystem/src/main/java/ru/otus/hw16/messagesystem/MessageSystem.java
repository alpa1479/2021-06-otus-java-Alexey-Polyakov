package ru.otus.hw16.messagesystem;

import ru.otus.hw16.client.MsClient;
import ru.otus.hw16.model.message.Message;
import ru.otus.hw16.model.resultdatatype.ResultDataType;

public interface MessageSystem {

    void start();

    void dispose() throws InterruptedException;

    void dispose(Runnable callback) throws InterruptedException;

    void addClient(MsClient client);

    void removeClient(String clientId);

    <T extends ResultDataType> boolean newMessage(Message<T> msg);

    int currentQueueSize();
}
