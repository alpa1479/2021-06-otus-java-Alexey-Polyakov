package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.cachehw.HwCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<Long, Client> cache;

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> clientDataTemplate) {
        this.transactionRunner = transactionRunner;
        this.clientDataTemplate = clientDataTemplate;
        this.cache = null;
    }

    public DbServiceClientImpl(TransactionRunner transactionRunner,
                               DataTemplate<Client> clientDataTemplate, HwCache<Long, Client> cache) {
        this.transactionRunner = transactionRunner;
        this.clientDataTemplate = clientDataTemplate;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            Long clientId = client.getId();
            if (clientId == null) {
                clientId = clientDataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                putToCache(clientId, createdClient);
                log.info("created client: {}", createdClient);
                return createdClient;
            }
            clientDataTemplate.update(connection, client);
            putToCache(clientId, client);
            log.info("updated client: {}", client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client client = getFromCache(id);
        if (client != null) {
            return Optional.of(client);
        }
        return transactionRunner.doInTransaction(connection -> {
            var clientOptional = clientDataTemplate.findById(connection, id);
            log.info("clconnection = {HikariProxyConnection@4888} \"HikariProxyConnection@937921860 wrapping org.postgresql.jdbc.PgConnection@77fdc5d\"ient: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = clientDataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }

    private Client getFromCache(long id) {
        return cache != null ? cache.get(id) : null;
    }

    private void putToCache(Long id, Client client) {
        Optional.ofNullable(cache).ifPresent(cache -> cache.put(id, client));
    }
}
