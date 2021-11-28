package ru.otus.hw16.crm.service.db;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.hw16.core.repository.ClientRepository;
import ru.otus.hw16.core.transactionmanager.TransactionManager;
import ru.otus.hw16.crm.model.Client;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbServiceClientImpl implements DbServiceClient {

    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final TransactionManager transactionManager;
    private final ClientRepository clientRepository;

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(() -> {
            var savedClient = clientRepository.save(client);
            log.info("saved client: {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        var clientOptional = clientRepository.findById(id);
        log.info("client: {}", clientOptional);
        return clientOptional;
    }

    @Override
    public List<Client> findAll() {
        var clientList = clientRepository.findAll();
        log.info("clientList: {}", clientList);
        return clientList;
    }
}
