package ru.otus.hw16.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw16.model.message.MessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HandlersStoreImpl implements HandlersStore {

    private static final Logger log = LoggerFactory.getLogger(HandlersStoreImpl.class);

    private final Map<MessageType, MessageHandler> handlers = new ConcurrentHashMap<>();

    @Override
    public MessageHandler getHandlerByType(MessageType type) {
        log.trace("getting handler with type {}", type.getName());
        return handlers.get(type);
    }

    @Override
    public void addHandler(MessageType type, MessageHandler handler) {
        log.trace("adding handler with type {}", type.getName());
        handlers.put(type, handler);
    }
}
