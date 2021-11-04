package ru.otus.hw12.crm.service;

import ru.otus.hw12.crm.model.Client;

import java.util.List;
import java.util.Optional;

public interface DbServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();
}
