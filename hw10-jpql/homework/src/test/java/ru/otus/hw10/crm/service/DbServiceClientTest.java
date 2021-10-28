package ru.otus.hw10.crm.service;

import org.hibernate.stat.EntityStatistics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw10.base.AbstractHibernateTest;
import ru.otus.hw10.crm.model.Address;
import ru.otus.hw10.crm.model.Client;
import ru.otus.hw10.crm.model.Phone;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Демо работы с hibernate (с абстракциями) должно ")
class DbServiceClientTest extends AbstractHibernateTest {

    @Test
    @DisplayName(" корректно сохранять, изменять и загружать клиента")
    void shouldCorrectSaveClient() {
        //given
        var client = new Client("Ivan");

        //when
        var savedClient = dbServiceClient.saveClient(client);
        System.out.println(savedClient);

        //then
        var loadedSavedClient = dbServiceClient.getClient(savedClient.getId());
        assertThat(loadedSavedClient).isPresent();
        assertThat(loadedSavedClient.get()).usingRecursiveComparison().isEqualTo(savedClient);

        //when
        var savedClientUpdated = loadedSavedClient.get().clone();
        savedClientUpdated.setName("updatedName");
        dbServiceClient.saveClient(savedClientUpdated);

        //then
        var loadedClient = dbServiceClient.getClient(savedClientUpdated.getId());
        assertThat(loadedClient).isPresent();
        assertThat(loadedClient.get()).usingRecursiveComparison().isEqualTo(savedClientUpdated);
        System.out.println(loadedClient);

        //when
        var clientList = dbServiceClient.findAll();

        //then
        assertThat(clientList.size()).isEqualTo(1);
        assertThat(clientList.get(0)).usingRecursiveComparison().isEqualTo(loadedClient.get());
    }

    @Test
    @DisplayName(" корректно сохранять, изменять и загружать клиента c адресом и телефонными номерами")
    void shouldCorrectSaveClientWithAddressAndPhones() {
        //given
        var client = new Client("Вася", new Address("address"));
        client.addPhone(new Phone("phone-1"))
                .addPhone(new Phone("phone-2"))
                .addPhone(new Phone("phone-3"));

        //when
        var savedClient = dbServiceClient.saveClient(client);
        System.out.println(savedClient);

        //then
        var loadedSavedClient = dbServiceClient.getClient(savedClient.getId());
        assertThat(loadedSavedClient).isPresent();
        assertThat(loadedSavedClient.get()).usingRecursiveComparison().isEqualTo(savedClient);

        //when
        var savedClientUpdated = loadedSavedClient.get().clone();
        savedClientUpdated.setName("updatedName");
        dbServiceClient.saveClient(savedClientUpdated);

        //then
        var loadedClient = dbServiceClient.getClient(savedClientUpdated.getId());
        assertThat(loadedClient).isPresent();
        assertThat(loadedClient.get()).usingRecursiveComparison().isEqualTo(savedClientUpdated);
        System.out.println(loadedClient);

        //when
        var clientList = dbServiceClient.findAll();

        //then
        assertThat(clientList.size()).isEqualTo(1);
        assertThat(clientList.get(0)).usingRecursiveComparison().isEqualTo(loadedClient.get());

        //then
        EntityStatistics clientUsageStatistics = getUsageStatistics(Client.class);
        assertThat(clientUsageStatistics.getLoadCount()).isEqualTo(4);
        assertThat(clientUsageStatistics.getInsertCount()).isEqualTo(1);
        assertThat(clientUsageStatistics.getUpdateCount()).isEqualTo(1);

        //then
        EntityStatistics addressUsageStatistics = getUsageStatistics(Address.class);
        assertThat(addressUsageStatistics.getInsertCount()).isEqualTo(1);
        assertThat(addressUsageStatistics.getUpdateCount()).isEqualTo(0);

        //then
        EntityStatistics phoneUsageStatistics = getUsageStatistics(Phone.class);
        assertThat(phoneUsageStatistics.getInsertCount()).isEqualTo(3);
        assertThat(phoneUsageStatistics.getUpdateCount()).isEqualTo(0);
    }
}