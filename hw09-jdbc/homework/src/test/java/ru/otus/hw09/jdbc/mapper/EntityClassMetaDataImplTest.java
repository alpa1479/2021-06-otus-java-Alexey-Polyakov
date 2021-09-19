package ru.otus.hw09.jdbc.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@DisplayName("EntityClassMetaDataImplTest should analyze class object")
public class EntityClassMetaDataImplTest {

    <T> EntityClassMetaData<T> setUp(Class<T> targetClass) {
        return new EntityClassMetaDataImpl<>(targetClass);
    }

    @ParameterizedTest(name = "{index} - Should return name for {0}")
    @MethodSource("provideArgumentsWithClassName")
    <T> void shouldReturnName(String displayName, Class<T> targetClass, String targetClassName) {
        EntityClassMetaData<T> entityClassMetaData = setUp(targetClass);
        var name = entityClassMetaData.getName();
        Assertions.assertThat(name).isEqualTo(targetClassName);
    }

    @ParameterizedTest(name = "{index} - Should return constructor for {0}")
    @MethodSource("provideArgumentsWithConstructor")
    <T> void shouldReturnConstructor(String displayName, Class<T> targetClass, Constructor<T> targetConstructor) {
        EntityClassMetaData<T> entityClassMetaData = setUp(targetClass);
        var constructor = entityClassMetaData.getConstructor();
        Assertions.assertThat(constructor).isEqualTo(targetConstructor);
    }

    @ParameterizedTest(name = "{index} - Should return id field for {0}")
    @MethodSource("provideArgumentsWithIdField")
    <T> void shouldReturnIdField(String displayName, Class<T> targetClass, Field targetIdField) {
        EntityClassMetaData<T> entityClassMetaData = setUp(targetClass);
        var idField = entityClassMetaData.getIdField();
        Assertions.assertThat(idField).isEqualTo(targetIdField);
    }

    @ParameterizedTest(name = "{index} - Should return all fields for {0}")
    @MethodSource("provideArgumentsWithAllFields")
    <T> void shouldReturnAllFields(String displayName, Class<T> targetClass, List<Field> allTargetFields) {
        EntityClassMetaData<T> entityClassMetaData = setUp(targetClass);
        var allFields = entityClassMetaData.getAllFields();
        Assertions.assertThat(allFields).isEqualTo(allTargetFields);
    }

    @ParameterizedTest(name = "{index} - Should return all fields without id for {0}")
    @MethodSource("provideArgumentsWithAllFieldsWithoutId")
    <T> void shouldReturnAllFieldsWithoutId(String displayName, Class<T> targetClass, List<Field> allTargetFieldsWithoutId) {
        EntityClassMetaData<T> entityClassMetaData = setUp(targetClass);
        var allFieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        Assertions.assertThat(allFieldsWithoutId).isEqualTo(allTargetFieldsWithoutId);
    }

    private static Stream<Arguments> provideArgumentsWithClassName() {
        return Stream.of(
                Arguments.of(Client.class.getSimpleName(), Client.class, "client"),
                Arguments.of(Manager.class.getSimpleName(), Manager.class, "manager")
        );
    }

    private static Stream<Arguments> provideArgumentsWithConstructor() throws NoSuchMethodException {
        return Stream.of(
                Arguments.of(Client.class.getSimpleName(), Client.class, Client.class.getConstructor()),
                Arguments.of(Manager.class.getSimpleName(), Manager.class, Manager.class.getConstructor())
        );
    }

    private static Stream<Arguments> provideArgumentsWithIdField() throws NoSuchFieldException {
        return Stream.of(
                Arguments.of(Client.class.getSimpleName(), Client.class, Client.class.getDeclaredField("id")),
                Arguments.of(Manager.class.getSimpleName(), Manager.class, Manager.class.getDeclaredField("no"))
        );
    }

    private static Stream<Arguments> provideArgumentsWithAllFields() throws NoSuchFieldException {
        final List<Field> clientFields = Arrays.asList(
                Client.class.getDeclaredField("id"),
                Client.class.getDeclaredField("name")
        );
        final List<Field> managerFields = Arrays.asList(
                Manager.class.getDeclaredField("no"),
                Manager.class.getDeclaredField("label"),
                Manager.class.getDeclaredField("param1")
        );
        return Stream.of(
                Arguments.of(Client.class.getSimpleName(), Client.class, clientFields),
                Arguments.of(Manager.class.getSimpleName(), Manager.class, managerFields)
        );
    }

    private static Stream<Arguments> provideArgumentsWithAllFieldsWithoutId() throws NoSuchFieldException {
        final List<Field> clientFields = Collections.singletonList(Client.class.getDeclaredField("name"));
        final List<Field> managerFields = Arrays.asList(
                Manager.class.getDeclaredField("label"),
                Manager.class.getDeclaredField("param1")
        );
        return Stream.of(
                Arguments.of(Client.class.getSimpleName(), Client.class, clientFields),
                Arguments.of(Manager.class.getSimpleName(), Manager.class, managerFields)
        );
    }
}
