package ru.otus.hw16.model.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.otus.hw16.model.resultdatatype.ResultDataType;
import ru.otus.hw16.model.resultdatatype.VoidResultDataType;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageBuilder {

    private static final Message<VoidResultDataType> VOID_MESSAGE = buildVoidMessage();

    public static Message<VoidResultDataType> getVoidMessage() {
        return VOID_MESSAGE;
    }

    public static <T extends ResultDataType> Message<T> buildMessage(MessageId sourceMessageId, String from,
                                                                     String to, T data,
                                                                     MessageType type, MessageCallback<T> callback) {
        String id = UUID.randomUUID().toString();
        return new Message<>(new MessageId(id), sourceMessageId, from, to, type, data, callback);
    }

    public static <T extends ResultDataType> Message<T> buildReplyMessage(Message<T> message, T data) {
        return buildMessage(message.getId(), message.getTo(), message.getFrom(), data, message.getType(), message.getCallback());
    }

    private static Message<VoidResultDataType> buildVoidMessage() {
        return buildMessage(null, null, null, new VoidResultDataType(), MessageType.VOID, null);
    }
}
