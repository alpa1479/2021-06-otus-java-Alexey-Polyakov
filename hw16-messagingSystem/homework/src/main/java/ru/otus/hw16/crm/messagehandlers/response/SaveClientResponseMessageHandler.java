package ru.otus.hw16.crm.messagehandlers.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.hw16.handlers.MessageHandler;
import ru.otus.hw16.model.message.Message;
import ru.otus.hw16.model.resultdatatype.ResultDataType;

import java.util.Optional;

@Component("saveClientResponseMessageHandler")
public class SaveClientResponseMessageHandler implements MessageHandler {

    private static final Logger log = LoggerFactory.getLogger(SaveClientResponseMessageHandler.class);

    @Override
    public <T extends ResultDataType> Optional<Message<T>> handle(Message<T> msg) {
        log.info("new message: {}", msg);
        try {
            var callback = msg.getCallback();
            if (callback != null) {
                callback.accept(msg.getData());
            } else {
                log.error("callback for Id: {} not found", msg.getId());
            }
        } catch (Exception ex) {
            log.error("msg: {}", msg, ex);
        }
        return Optional.empty();
    }
}
