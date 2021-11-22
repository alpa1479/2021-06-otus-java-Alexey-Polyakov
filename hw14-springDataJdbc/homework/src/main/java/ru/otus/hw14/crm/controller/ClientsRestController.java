package ru.otus.hw14.crm.controller;

import org.springframework.web.bind.annotation.*;
import ru.otus.hw14.crm.model.Client;
import ru.otus.hw14.crm.service.DbServiceClient;

@RestController
public class ClientsRestController {

    private final DbServiceClient dbServiceClient;

    public ClientsRestController(DbServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping("/api/clients/{id}")
    public Client getClientById(@PathVariable(name = "id") long id) {
        return dbServiceClient.getClient(id).orElse(null);
    }

    @PostMapping("/api/clients")
    public Client saveClient(@RequestBody Client client) {
        return dbServiceClient.saveClient(client);
    }
}
