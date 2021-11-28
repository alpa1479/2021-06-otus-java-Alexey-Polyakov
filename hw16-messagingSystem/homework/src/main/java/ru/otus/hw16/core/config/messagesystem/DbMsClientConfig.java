package ru.otus.hw16.core.config.messagesystem;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw16.client.MsClient;
import ru.otus.hw16.client.MsClientImpl;
import ru.otus.hw16.handlers.HandlersStore;
import ru.otus.hw16.handlers.HandlersStoreImpl;
import ru.otus.hw16.handlers.MessageHandler;
import ru.otus.hw16.messagesystem.MessageSystem;
import ru.otus.hw16.model.message.MessageType;

@Configuration
public class DbMsClientConfig {

    @Bean
    public HandlersStore dbHandlersStore(@Qualifier("saveClientRequestMessageHandler") MessageHandler messageHandler) {
        HandlersStoreImpl handlersStore = new HandlersStoreImpl();
        handlersStore.addHandler(MessageType.SAVE_CLIENT, messageHandler);
        return handlersStore;
    }

    @Bean
    public MsClient dbMsClient(MessageSystem messageSystem,
                               @Qualifier("dbHandlersStore") HandlersStore handlersStore) {
        MsClientImpl dbMsClient = new MsClientImpl("dbMsClient", messageSystem, handlersStore);
        messageSystem.addClient(dbMsClient);
        return dbMsClient;
    }
}
