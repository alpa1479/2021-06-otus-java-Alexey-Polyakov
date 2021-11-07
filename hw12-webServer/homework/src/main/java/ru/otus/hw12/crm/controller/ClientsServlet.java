package ru.otus.hw12.crm.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.hw12.core.template.TemplateProcessor;
import ru.otus.hw12.crm.model.Client;
import ru.otus.hw12.crm.service.DbServiceClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_CLIENTS = "clients";

    private final TemplateProcessor templateProcessor;
    private final DbServiceClient dbServiceClient;

    public ClientsServlet(TemplateProcessor templateProcessor, DbServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Client> clients = dbServiceClient.findAll();

        Map<String, Object> templateParameters = new HashMap<>();
        templateParameters.put(TEMPLATE_ATTR_CLIENTS, clients);

        String page = templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, templateParameters);
        resp.setContentType("text/html");
        resp.getWriter().println(page);
    }
}
