package ru.otus.hw16.crm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw16.crm.model.Client;
import ru.otus.hw16.crm.service.db.DbServiceClient;

@RestController
@RequiredArgsConstructor
public class ClientsRestController {

    private final DbServiceClient dbServiceClient;

    @GetMapping("/api/clients/{id}")
    public Client getClientById(@PathVariable(name = "id") long id) {
        return dbServiceClient.getClient(id).orElse(null);
    }

    @PostMapping("/api/clients")
    public Client saveClient(@RequestBody Client client) {
        return dbServiceClient.saveClient(client);
    }
}
