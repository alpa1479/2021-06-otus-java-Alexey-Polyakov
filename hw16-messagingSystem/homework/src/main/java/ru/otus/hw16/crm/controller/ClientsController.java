package ru.otus.hw16.crm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw16.crm.model.Client;
import ru.otus.hw16.crm.service.db.DbServiceClient;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ClientsController {

    private final DbServiceClient dbServiceClient;

    @GetMapping({"/", "/clients"})
    public String clientsListView(Model model) {
        List<Client> clients = dbServiceClient.findAll();
        model.addAttribute("clients", clients);
        return "clients";
    }
}
