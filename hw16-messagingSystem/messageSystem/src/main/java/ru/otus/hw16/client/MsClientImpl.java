package ru.otus.hw16.client;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw16.handlers.HandlersStore;
import ru.otus.hw16.handlers.MessageHandler;
import ru.otus.hw16.messagesystem.MessageSystem;
import ru.otus.hw16.model.message.Message;
import ru.otus.hw16.model.message.MessageBuilder;
import ru.otus.hw16.model.message.MessageCallback;
import ru.otus.hw16.model.message.MessageType;
import ru.otus.hw16.model.resultdatatype.ResultDataType;

@RequiredArgsConstructor
@EqualsAndHashCode(of = "name")
public class MsClientImpl implements MsClient {

    private static final Logger log = LoggerFactory.getLogger(MsClientImpl.class);

    @NonNull
    private final String name;
    private final MessageSystem messageSystem;
    private final HandlersStore handlersStore;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public <T extends ResultDataType> Message<T> produceMessage(String to, T data,
                                                                MessageType type, MessageCallback<T> callback) {
        return MessageBuilder.buildMessage(null, name, to, data, type, callback);
    }

    @Override
    public <T extends ResultDataType> boolean sendMessage(Message<T> msg) {
        boolean result = messageSystem.newMessage(msg);
        if (!result) {
            log.error("last message was rejected: {}", msg);
        }
        return result;
    }

    @Override
    public <T extends ResultDataType> void handle(Message<T> msg) {
        log.info("new message: {}", msg);
        try {
            MessageHandler messageHandler = handlersStore.getHandlerByType(msg.getType());
            if (messageHandler != null) {
                messageHandler.handle(msg).ifPresent(this::sendMessage);
            } else {
                log.error("handler not found for the message type: {}", msg.getType());
            }
        } catch (Exception e) {
            log.info("error during message processing: {}", msg, e);
        }
    }
}
