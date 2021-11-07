package ru.otus.hw12.crm.controller;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.hw12.core.util.RequestParametersUtil;
import ru.otus.hw12.crm.model.Client;
import ru.otus.hw12.crm.service.DbServiceClient;

import java.io.IOException;
import java.io.Reader;

public class ClientsRestServlet extends HttpServlet {

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    private final Gson gson;
    private final DbServiceClient dbServiceClient;

    public ClientsRestServlet(Gson gson, DbServiceClient dbServiceClient) {
        this.gson = gson;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long id = RequestParametersUtil.getIdPathParameter(req);
        Client client = dbServiceClient.getClient(id).orElse(null);

        resp.setContentType(CONTENT_TYPE);
        resp.getOutputStream().print(gson.toJson(client));
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Reader reader = req.getReader()) {
            Client client = gson.fromJson(reader, Client.class);
            Client createdClient = dbServiceClient.saveClient(client);

            resp.setContentType(CONTENT_TYPE);
            resp.getOutputStream().print(gson.toJson(createdClient));
        }
    }
}
