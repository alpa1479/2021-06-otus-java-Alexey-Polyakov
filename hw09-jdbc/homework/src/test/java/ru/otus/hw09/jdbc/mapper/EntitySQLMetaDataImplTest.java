package ru.otus.hw09.jdbc.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;

import java.util.stream.Stream;

@DisplayName("EntitySQLMetaDataImplTest should create SQL for specified class")
public class EntitySQLMetaDataImplTest {

    <T> EntitySQLMetaData setUp(Class<T> targetClass) {
        return new EntitySQLMetaDataImpl(new EntityClassMetaDataImpl<>(targetClass));
    }

    @ParameterizedTest(name = "{index} - Should return select all SQL for {0}")
    @MethodSource("provideArgumentsWithSelectAllSql")
    <T> void shouldReturnSelectAllSql(String displayName, Class<T> targetClass, String targetSelectAllSql) {
        EntitySQLMetaData entitySQLMetaData = setUp(targetClass);
        var selectAllSql = entitySQLMetaData.getSelectAllSql();
        Assertions.assertThat(selectAllSql).isEqualTo(targetSelectAllSql);
    }

    @ParameterizedTest(name = "{index} - Should return select by id SQL for {0}")
    @MethodSource("provideArgumentsWithSelectByIdSql")
    <T> void shouldReturnSelectByIdSql(String displayName, Class<T> targetClass, String targetSelectByIdSql) {
        EntitySQLMetaData entitySQLMetaData = setUp(targetClass);
        var selectByIdSql = entitySQLMetaData.getSelectByIdSql();
        Assertions.assertThat(selectByIdSql).isEqualTo(targetSelectByIdSql);
    }

    @ParameterizedTest(name = "{index} - Should return insert SQL for {0}")
    @MethodSource("provideArgumentsWithInsertSql")
    <T> void shouldReturnInsertSql(String displayName, Class<T> targetClass, String targetInsertSql) {
        EntitySQLMetaData entitySQLMetaData = setUp(targetClass);
        var insertSql = entitySQLMetaData.getInsertSql();
        Assertions.assertThat(insertSql).isEqualTo(targetInsertSql);
    }

    @ParameterizedTest(name = "{index} - Should return update SQL for {0}")
    @MethodSource("provideArgumentsWithUpdateSql")
    <T> void shouldReturnUpdateSql(String displayName, Class<T> targetClass, String targetUpdateSql) {
        EntitySQLMetaData entitySQLMetaData = setUp(targetClass);
        var updateSql = entitySQLMetaData.getUpdateSql();
        Assertions.assertThat(updateSql).isEqualTo(targetUpdateSql);
    }


    private static Stream<Arguments> provideArgumentsWithSelectAllSql() {
        return Stream.of(
                Arguments.of(Client.class.getSimpleName(), Client.class, "select * from client"),
                Arguments.of(Manager.class.getSimpleName(), Manager.class, "select * from manager")
        );
    }

    private static Stream<Arguments> provideArgumentsWithSelectByIdSql() {
        return Stream.of(
                Arguments.of(Client.class.getSimpleName(), Client.class, "select * from client where id = ?"),
                Arguments.of(Manager.class.getSimpleName(), Manager.class, "select * from manager where no = ?")
        );
    }

    private static Stream<Arguments> provideArgumentsWithInsertSql() {
        return Stream.of(
                Arguments.of(Client.class.getSimpleName(), Client.class, "insert into client(name) values (?)"),
                Arguments.of(Manager.class.getSimpleName(), Manager.class, "insert into manager(label,param1) values (?, ?)")
        );
    }

    private static Stream<Arguments> provideArgumentsWithUpdateSql() {
        return Stream.of(
                Arguments.of(Client.class.getSimpleName(), Client.class, "update client set name = ? where id = ?"),
                Arguments.of(Manager.class.getSimpleName(), Manager.class, "update manager set label = ?, param1 = ? where no = ?")
        );
    }
}
