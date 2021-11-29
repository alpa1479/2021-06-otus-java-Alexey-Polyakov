package ru.otus.hw16.crm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw16.crm.model.ClientsList;
import ru.otus.hw16.crm.service.db.DbServiceClient;

@Controller
@RequiredArgsConstructor
public class ClientsController {

    private final DbServiceClient dbServiceClient;

    @GetMapping({"/", "/clients"})
    public String clientsListView(Model model) {
        ClientsList clientsList = dbServiceClient.findAll();
        model.addAttribute("clientsList", clientsList);
        return "clients";
    }
}
