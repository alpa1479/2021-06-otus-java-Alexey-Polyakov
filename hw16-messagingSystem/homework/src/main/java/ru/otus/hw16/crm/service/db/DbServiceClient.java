package ru.otus.hw16.crm.service.db;

import ru.otus.hw16.crm.model.Client;

import java.util.List;
import java.util.Optional;

public interface DbServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();
}
