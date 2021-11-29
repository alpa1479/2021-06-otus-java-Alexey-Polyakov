package ru.otus.hw16.client;

import ru.otus.hw16.model.message.Message;
import ru.otus.hw16.model.message.MessageCallback;
import ru.otus.hw16.model.message.MessageType;
import ru.otus.hw16.model.resultdatatype.ResultDataType;

public interface MsClient {

    String getName();

    <T extends ResultDataType> Message<T> produceMessage(String to, T data,
                                                         MessageType type, MessageCallback<T> callback);

    <T extends ResultDataType> boolean sendMessage(Message<T> msg);

    <T extends ResultDataType> void handle(Message<T> msg);
}
