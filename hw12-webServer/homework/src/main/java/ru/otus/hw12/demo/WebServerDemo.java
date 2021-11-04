package ru.otus.hw12.demo;

import com.google.gson.GsonBuilder;
import org.hibernate.cfg.Configuration;
import ru.otus.hw12.core.repository.DataTemplateHibernate;
import ru.otus.hw12.core.repository.HibernateUtils;
import ru.otus.hw12.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.hw12.core.template.TemplateProcessorImpl;
import ru.otus.hw12.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.hw12.crm.model.Address;
import ru.otus.hw12.crm.model.Admin;
import ru.otus.hw12.crm.model.Client;
import ru.otus.hw12.crm.model.Phone;
import ru.otus.hw12.crm.service.AuthenticationServiceImpl;
import ru.otus.hw12.crm.service.DbServiceAdminImpl;
import ru.otus.hw12.crm.service.DbServiceClientImpl;
import ru.otus.hw12.server.ClientsWebServer;
import ru.otus.hw12.server.ClientsWebServerImpl;

public class WebServerDemo {

    public static final int WEB_SERVER_PORT = 8080;
    public static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        var sessionFactory = HibernateUtils.buildSessionFactory(
                configuration,
                Client.class, Address.class, Phone.class, Admin.class
        );

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var adminTemplate = new DataTemplateHibernate<>(Admin.class);
        var dbServiceAdmin = new DbServiceAdminImpl(transactionManager, adminTemplate);
        var authenticationService = new AuthenticationServiceImpl(dbServiceAdmin);

        // create first admin
        Admin admin = new Admin(1L, "admin", "admin");
        dbServiceAdmin.saveAdmin(admin);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        var gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        var templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        ClientsWebServer clientsWebServer = new ClientsWebServerImpl(
                WEB_SERVER_PORT, gson, templateProcessor, dbServiceClient, authenticationService
        );

        clientsWebServer.start();
        clientsWebServer.join();
    }
}
