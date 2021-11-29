package ru.otus.hw16.crm.messagehandlers.request;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.hw16.crm.model.ClientsList;
import ru.otus.hw16.crm.service.db.DbServiceClient;
import ru.otus.hw16.handlers.MessageHandler;
import ru.otus.hw16.model.message.Message;
import ru.otus.hw16.model.message.MessageBuilder;
import ru.otus.hw16.model.resultdatatype.ResultDataType;

import java.util.Optional;

@Component("getClientsRequestMessageHandler")
@RequiredArgsConstructor
public class GetClientsRequestMessageHandler implements MessageHandler {

    private static final Logger log = LoggerFactory.getLogger(GetClientsRequestMessageHandler.class);

    private final DbServiceClient dbServiceClient;

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ResultDataType> Optional<Message<T>> handle(Message<T> msg) {
        log.info("new message: {}", msg);
        ClientsList clientsList = dbServiceClient.findAll();
        return Optional.of(MessageBuilder.buildReplyMessage(msg, (T) clientsList));
    }
}
