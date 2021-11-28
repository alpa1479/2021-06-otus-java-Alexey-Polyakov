package ru.otus.hw16.crm.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.hw16.crm.model.Client;
import ru.otus.hw16.crm.service.frontend.FrontendService;

@Controller
@RequiredArgsConstructor
public class ClientsWsController {

    private static final Logger log = LoggerFactory.getLogger(ClientsWsController.class);

    private final FrontendService frontendService;
    private final SimpMessagingTemplate template;

    @MessageMapping("/client")
    public void getClient(Client client) {
        log.info("got client: {}", client);
        frontendService.saveClient(client, this::sendSavedClient);
    }

    private void sendSavedClient(Client savedClient) {
        template.convertAndSend("/topic/client/response", savedClient);
    }
}
