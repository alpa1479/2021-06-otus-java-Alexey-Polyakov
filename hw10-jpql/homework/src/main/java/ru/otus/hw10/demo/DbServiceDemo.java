package ru.otus.hw10.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw10.core.repository.DataTemplateHibernate;
import ru.otus.hw10.core.repository.HibernateUtils;
import ru.otus.hw10.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.hw10.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.hw10.crm.model.Address;
import ru.otus.hw10.crm.model.Client;
import ru.otus.hw10.crm.model.Phone;
import ru.otus.hw10.crm.service.DbServiceClientImpl;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");
//
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration,
                Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        final Phone phone = new Phone("1");
        final Phone phone1 = new Phone("2");
        final Phone phone2 = new Phone("3");
        var address = new Address("address-1");
        var client = new Client("client-1", address);
        client.addPhone(phone).addPhone(phone1).addPhone(phone2);
        dbServiceClient.saveClient(client);

        final Phone phone3 = new Phone("4");
        final Phone phone4 = new Phone("5");
        final Phone phone5 = new Phone("6");
        var address2 = new Address("address-2");
        var client2 = new Client("client-2", address2);
        client2.addPhone(phone3).addPhone(phone4).addPhone(phone5);
        var savedClient2 = dbServiceClient.saveClient(client2);
        var client2Selected = dbServiceClient.getClient(savedClient2.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + savedClient2.getId()));
        log.info("client2Selected:{}", client2Selected);
///
        dbServiceClient.saveClient(new Client(client2Selected.getId(), "updateName", new Address("updatedAddress")));
        var clientUpdated = dbServiceClient.getClient(client2Selected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client2Selected.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        log.info("All clients");
        dbServiceClient.findAll().forEach(retrievedClient -> log.info("client:{}", retrievedClient));
    }
}
