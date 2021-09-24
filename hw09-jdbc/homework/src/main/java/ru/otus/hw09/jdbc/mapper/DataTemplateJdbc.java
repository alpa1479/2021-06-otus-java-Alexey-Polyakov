package ru.otus.hw09.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.hw09.util.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntityClassMetaData<T> entityClassMetaData;
    private final EntitySQLMetaData entitySQLMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entityClassMetaData = entityClassMetaData;
        this.entitySQLMetaData = new EntitySQLMetaDataImpl(entityClassMetaData);
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                T object = null;
                if (rs.next()) {
                    object = instantiateObjectFromResultSet(rs);
                }
                return object;
            } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var objects = new ArrayList<T>();
            try {
                while (rs.next()) {
                    T object = instantiateObjectFromResultSet(rs);
                    objects.add(object);
                }
                return objects;
            } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T object) {
        try {
            final List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
            final List<Object> fieldValues = Reflections.getFieldValues(fieldsWithoutId, object);
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), fieldValues);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        try {
            final List<Field> fields = entityClassMetaData.getFieldsWithoutId();
            final List<Object> fieldValues = Reflections.getFieldValues(fields, object);
            final Object idFieldValue = Reflections.getFieldValue(entityClassMetaData.getIdField(), object);
            fieldValues.add(idFieldValue);
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), fieldValues);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T instantiateObjectFromResultSet(ResultSet rs)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, SQLException {
        final Constructor<T> constructor = entityClassMetaData.getConstructor();
        final T object = Reflections.instantiate(constructor);
        return fillObjectFieldsFromResultSet(object, entityClassMetaData.getAllFields(), rs);
    }

    private T fillObjectFieldsFromResultSet(T object, List<Field> fields, ResultSet rs)
            throws IllegalAccessException, SQLException {
        for (Field field : fields) {
            final int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                if (Reflections.isNotAccessibleField(modifiers)) {
                    field.setAccessible(true);
                }
                field.set(object, rs.getObject(field.getName()));
            }
        }
        return object;
    }
}
