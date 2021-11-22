package ru.otus.hw14.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw14.crm.model.Client;
import ru.otus.hw14.crm.service.DbServiceClient;

import java.util.List;

@Controller
public class ClientsController {

    private final DbServiceClient dbServiceClient;

    public ClientsController(DbServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping({"/", "/clients"})
    public String clientsListView(Model model) {
        List<Client> clients = dbServiceClient.findAll();
        model.addAttribute("clients", clients);
        return "clients";
    }
}
