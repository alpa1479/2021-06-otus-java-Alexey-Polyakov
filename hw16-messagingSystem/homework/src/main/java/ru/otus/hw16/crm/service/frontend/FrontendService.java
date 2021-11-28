package ru.otus.hw16.crm.service.frontend;

import ru.otus.hw16.crm.model.Client;
import ru.otus.hw16.model.message.MessageCallback;

public interface FrontendService {

    void saveClient(Client client, MessageCallback<Client> clientConsumer);
}
