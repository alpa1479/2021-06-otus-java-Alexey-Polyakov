package ru.otus.hw16.core.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.hw16.crm.model.Address;
import ru.otus.hw16.crm.model.Client;
import ru.otus.hw16.crm.model.Phone;
import ru.otus.hw16.testcontainers.BasePersistenceTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ClientRepositoryTest extends BasePersistenceTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    @DisplayName("should find all clients")
    @Sql
    void shouldFindAllClients() {
        // when
        List<Client> clients = clientRepository.findAll();

        // then
        Assertions.assertThat(clients).hasSize(3);
    }

    @Test
    @DisplayName("should save client with address and phones")
    void shouldSaveClientWithAddressAndPhones() {
        // given
        Client client = prepareClient();

        // when
        Client savedClient = clientRepository.save(client);

        // then
        Assertions.assertThat(savedClient).isEqualTo(client);
    }

    @Test
    @DisplayName("should find client by id with address and phones")
    void shouldFindClientByIdWithAddressAndPhones() {
        // given
        Client client = prepareClient();
        Client savedClient = clientRepository.save(client);

        // when
        Optional<Client> retrievedClient = clientRepository.findById(savedClient.getId());

        // then
        Assertions.assertThat(retrievedClient).isPresent().get().isEqualTo(client);
    }

    @Test
    @DisplayName("should update client name and remove address and phones")
    void shouldUpdateClientNameAndRemoveAddressAndPhones() {
        // given
        Client client = prepareClient();
        Client savedClient = clientRepository.save(client);

        // when
        savedClient.setName("updated-name");
        savedClient.setAddress(null);
        savedClient.setPhones(new HashSet<>());
        Client updatedClient = clientRepository.save(client);
        Optional<Client> retrievedClientOptional = clientRepository.findById(updatedClient.getId());

        // then
        Client retrievedClient = retrievedClientOptional.orElseThrow(IllegalStateException::new);
        Assertions.assertThat(retrievedClient).isEqualTo(client);
        Assertions.assertThat(retrievedClient.getAddress()).isNull();
        Assertions.assertThat(retrievedClient.isEmptyPhones()).isTrue();
    }

    @Test
    @DisplayName("should remove client with address and phones")
    void shouldRemoveClientWithAddressAndPhones() {
        // given
        Client client = prepareClient();
        Client savedClient = clientRepository.save(client);

        // when
        clientRepository.delete(savedClient);
        Optional<Client> retrievedClient = clientRepository.findById(savedClient.getId());

        // then
        Assertions.assertThat(retrievedClient).isNotPresent();
    }

    private Client prepareClient() {
        Address address = new Address("test-street");
        Set<Phone> phones = Set.of(
                new Phone("test-number-1"),
                new Phone("test-number-2"),
                new Phone("test-number-3")
        );
        return new Client("test-name", address, phones);
    }

}
