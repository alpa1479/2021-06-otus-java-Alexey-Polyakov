package ru.otus.hw16.crm.service.db;

import ru.otus.hw16.crm.model.Client;
import ru.otus.hw16.crm.model.ClientsList;

import java.util.Optional;

public interface DbServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    ClientsList findAll();
}
