package ru.otus.hw09.jdbc.mapper;

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

    private final Class<T> targetClass;

    public EntityClassMetaDataImpl(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public String getName() {
        return targetClass.getSimpleName().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public Constructor<T> getConstructor() {
        Constructor<T> constructor = null;
        try {
            constructor = targetClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            log.error("Cant find constructor for the provided class", e);
        }
        return constructor;
    }

    @Override
    public Field getIdField() {
        return Arrays.stream(targetClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(targetClass.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(targetClass.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
    }
}
