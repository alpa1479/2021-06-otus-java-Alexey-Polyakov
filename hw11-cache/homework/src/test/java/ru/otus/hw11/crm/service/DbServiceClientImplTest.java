package ru.otus.hw11.crm.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.otus.core.cachehw.HwCache;
import ru.otus.core.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.hw11.jdbc.mapper.DataTemplateJdbc;
import ru.otus.hw11.jdbc.mapper.EntityClassMetaData;
import ru.otus.hw11.jdbc.mapper.EntityClassMetaDataImpl;

import java.util.Optional;
import java.util.stream.Stream;

@DisplayName("DbServiceClientImplTest should insert object in DB and Cache and select object from DB or Cache")
public class DbServiceClientImplTest {

    private static PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER;

    @BeforeAll
    public static void init() {
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:12-alpine")
                .withDatabaseName("testDataBase")
                .withUsername("owner")
                .withPassword("secret")
                .withClasspathResourceMapping("00_createTables.sql", "/docker-entrypoint-initdb.d/00_createTables.sql", BindMode.READ_ONLY);
        POSTGRE_SQL_CONTAINER.start();
    }

    @AfterAll
    public static void shutdown() {
        POSTGRE_SQL_CONTAINER.stop();
    }

    DataTemplate<Client> setUpClientDataTemplate() {
        DbExecutor dbExecutor = new DbExecutorImpl();
        EntityClassMetaData<Client> entityClassMetaData = new EntityClassMetaDataImpl<>(Client.class);
        return new DataTemplateJdbc<>(dbExecutor, entityClassMetaData);
    }

    TransactionRunner setUpTransactionRunner() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                POSTGRE_SQL_CONTAINER.getJdbcUrl(), POSTGRE_SQL_CONTAINER.getUsername(), POSTGRE_SQL_CONTAINER.getPassword()
        );
        return new TransactionRunnerJdbc(dataSource);
    }

    DBServiceClient setUpDbServiceClient(HwCache<Long, Client> cache) {
        TransactionRunner transactionRunner = setUpTransactionRunner();
        DataTemplate<Client> clientDataTemplate = setUpClientDataTemplate();
        return new DbServiceClientImpl(transactionRunner, clientDataTemplate, cache);
    }

    @ParameterizedTest(name = "{index} - Should save client to DB and get from {0}")
    @MethodSource("provideArgumentsWithCache")
    void shouldSaveClientAndGetFromCache(String displayName, HwCache<Long, Client> clientHwCache) {
        // given
        DBServiceClient dbServiceClient = setUpDbServiceClient(clientHwCache);

        //when
        Client client = new Client("test-name");
        Client savedClient = dbServiceClient.saveClient(client);

        //then
        for (int i = 0; i < 10; i++) {
            Optional<Client> fetchedClient = dbServiceClient.getClient(savedClient.getId());
            Assertions.assertThat(fetchedClient).isPresent().get().isEqualTo(savedClient);
        }
    }

    private static Stream<Arguments> provideArgumentsWithCache() {
        return Stream.of(
                Arguments.of("cache", new MyCache<>()),
                Arguments.of("DB", null)
        );
    }
}
