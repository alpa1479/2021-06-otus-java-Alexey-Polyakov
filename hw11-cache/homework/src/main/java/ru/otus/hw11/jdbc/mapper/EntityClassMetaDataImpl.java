package ru.otus.hw11.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.crm.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private static final Logger log = LoggerFactory.getLogger(EntityClassMetaDataImpl.class);

    private final String name;
    private Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> allFieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> targetClass) {
        this.name = targetClass.getSimpleName().toLowerCase(Locale.ENGLISH);

        try {
            this.constructor = targetClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            log.error("Cant find constructor for the provided class", e);
        }

        final Field[] declaredFields = targetClass.getDeclaredFields();
        this.idField = Arrays.stream(declaredFields)
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElse(null);
        this.allFields = Arrays.asList(declaredFields);
        this.allFieldsWithoutId = Arrays.stream(declaredFields)
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return allFieldsWithoutId;
    }
}
