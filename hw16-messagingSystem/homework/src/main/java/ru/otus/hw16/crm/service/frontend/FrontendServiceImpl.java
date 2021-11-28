package ru.otus.hw16.crm.service.frontend;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.hw16.client.MsClient;
import ru.otus.hw16.crm.model.Client;
import ru.otus.hw16.model.message.Message;
import ru.otus.hw16.model.message.MessageCallback;
import ru.otus.hw16.model.message.MessageType;

@Service
public class FrontendServiceImpl implements FrontendService {

    private final String targetClientName;
    private final MsClient frontendMsClient;

    public FrontendServiceImpl(@Value("dbMsClient") String targetClientName,
                               @Qualifier("frontendMsClient") MsClient frontendMsClient) {
        this.targetClientName = targetClientName;
        this.frontendMsClient = frontendMsClient;
    }

    @Override
    public void saveClient(Client client, MessageCallback<Client> clientConsumer) {
        Message<Client> message = frontendMsClient.produceMessage(targetClientName, client, MessageType.SAVE_CLIENT, clientConsumer);
        frontendMsClient.sendMessage(message);
    }
}
