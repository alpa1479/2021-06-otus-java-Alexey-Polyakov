package ru.otus.hw09.jdbc.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Testcontainers
@DisplayName("DataTemplateJdbcTest should insert object in DB and select object from DB")
public class DataTemplateJdbcTest {

    @Container
    private final static PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:12-alpine")
            .withDatabaseName("testDataBase")
            .withUsername("owner")
            .withPassword("secret")
            .withClasspathResourceMapping("00_createTables.sql", "/docker-entrypoint-initdb.d/00_createTables.sql", BindMode.READ_ONLY)
            .withClasspathResourceMapping("01_insertData.sql", "/docker-entrypoint-initdb.d/01_insertData.sql", BindMode.READ_ONLY);

    @ParameterizedTest(name = "{index} - Should return selected objects for {0}")
    @MethodSource("provideArgumentsForSelect")
    <T> void shouldSelectAll(String displayName, Class<T> targetClass, List<T> targetItems) throws SQLException {
        DbExecutor dbExecutor = new DbExecutorImpl();
        EntityClassMetaData<T> entityClassMetaData = new EntityClassMetaDataImpl<>(targetClass);
        DataTemplate<T> dataTemplate = new DataTemplateJdbc<>(dbExecutor, entityClassMetaData);
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                POSTGRE_SQL_CONTAINER.getJdbcUrl(), POSTGRE_SQL_CONTAINER.getUsername(), POSTGRE_SQL_CONTAINER.getPassword()
        );
        Connection connection = dataSource.getConnection();

        List<?> selectedItems = dataTemplate.findAll(connection);
        Assertions.assertThat(selectedItems).isEqualTo(targetItems);
    }

    @ParameterizedTest(name = "{index} - Should return selected object by id for {0}")
    @MethodSource("provideArgumentsForSelectById")
    <T> void shouldSelectById(String displayName, Class<T> targetClass, Object targetItem) throws SQLException {
        DbExecutor dbExecutor = new DbExecutorImpl();
        EntityClassMetaData<T> entityClassMetaData = new EntityClassMetaDataImpl<>(targetClass);
        DataTemplate<T> dataTemplate = new DataTemplateJdbc<>(dbExecutor, entityClassMetaData);
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                POSTGRE_SQL_CONTAINER.getJdbcUrl(), POSTGRE_SQL_CONTAINER.getUsername(), POSTGRE_SQL_CONTAINER.getPassword()
        );
        Connection connection = dataSource.getConnection();

        Optional<?> selectedItem = dataTemplate.findById(connection, 1L);
        Assertions.assertThat(selectedItem).isPresent().get().isEqualTo(targetItem);
    }

    @ParameterizedTest(name = "{index} - Should insert objects for {0}")
    @MethodSource("provideArgumentsForInsert")
    <T> void shouldInsert(String displayName, Class<T> targetClass, T insertObject, T targetObject) throws SQLException {
        DbExecutor dbExecutor = new DbExecutorImpl();
        EntityClassMetaData<T> entityClassMetaData = new EntityClassMetaDataImpl<>(targetClass);
        DataTemplate<T> dataTemplate = new DataTemplateJdbc<>(dbExecutor, entityClassMetaData);
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                POSTGRE_SQL_CONTAINER.getJdbcUrl(), POSTGRE_SQL_CONTAINER.getUsername(), POSTGRE_SQL_CONTAINER.getPassword()
        );
        Connection connection = dataSource.getConnection();

        long insertedItemId = dataTemplate.insert(connection, insertObject);
        Optional<T> selectedItem = dataTemplate.findById(connection, insertedItemId);
        Assertions.assertThat(selectedItem).isPresent().get().isEqualTo(targetObject);
    }

    @ParameterizedTest(name = "{index} - Should update objects for {0}")
    @MethodSource("provideArgumentsForUpdate")
    <T> void shouldUpdate(String displayName, Class<T> targetClass, T updateObject, long updateObjectId) throws SQLException {
        DbExecutor dbExecutor = new DbExecutorImpl();
        EntityClassMetaData<T> entityClassMetaData = new EntityClassMetaDataImpl<>(targetClass);
        DataTemplate<T> dataTemplate = new DataTemplateJdbc<>(dbExecutor, entityClassMetaData);
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                POSTGRE_SQL_CONTAINER.getJdbcUrl(), POSTGRE_SQL_CONTAINER.getUsername(), POSTGRE_SQL_CONTAINER.getPassword()
        );
        Connection connection = dataSource.getConnection();

        dataTemplate.update(connection, updateObject);
        Optional<T> selectedItem = dataTemplate.findById(connection, updateObjectId);
        Assertions.assertThat(selectedItem).isPresent().get().isEqualTo(updateObject);
    }

    private static Stream<Arguments> provideArgumentsForSelect() {
        List<Client> clients = Arrays.asList(
                new Client(1L, "name_value_1"),
                new Client(2L, "name_value_2"),
                new Client(3L, "name_value_3")
        );
        List<Manager> managers = Arrays.asList(
                new Manager(1L, "label_value_1", "param1_value_1"),
                new Manager(2L, "label_value_2", "param1_value_2"),
                new Manager(3L, "label_value_3", "param1_value_3")
        );
        return Stream.of(
                Arguments.of(Client.class.getSimpleName(), Client.class, clients),
                Arguments.of(Manager.class.getSimpleName(), Manager.class, managers)
        );
    }

    private static Stream<Arguments> provideArgumentsForSelectById() {
        return Stream.of(
                Arguments.of(Client.class.getSimpleName(), Client.class, new Client(1L, "name_value_1")),
                Arguments.of(Manager.class.getSimpleName(), Manager.class, new Manager(1L, "label_value_1", "param1_value_1"))
        );
    }

    private static Stream<Arguments> provideArgumentsForInsert() {
        Client clientToInsert = new Client("inserted_client_name_value");
        Client clientToCheck = new Client(4L, "inserted_client_name_value");

        Manager managerToInsert = new Manager("inserted_manager_label_value", "inserted_manager_param1_value");
        Manager managerToCheck = new Manager(4L, "inserted_manager_label_value", "inserted_manager_param1_value");

        return Stream.of(
                Arguments.of(Client.class.getSimpleName(), Client.class, clientToInsert, clientToCheck),
                Arguments.of(Manager.class.getSimpleName(), Manager.class, managerToInsert, managerToCheck)
        );
    }

    private static Stream<Arguments> provideArgumentsForUpdate() {
        long updatedClientId = 2L;
        Client clientToUpdate = new Client(updatedClientId, "updated_client_name_value");

        long updatedManagerId = 2L;
        Manager managerToUpdate = new Manager(updatedManagerId, "updated_manager_label_value", "updated_manager_param1_value");

        return Stream.of(
                Arguments.of(Client.class.getSimpleName(), Client.class, clientToUpdate, updatedClientId),
                Arguments.of(Manager.class.getSimpleName(), Manager.class, managerToUpdate, updatedManagerId)
        );
    }
}
