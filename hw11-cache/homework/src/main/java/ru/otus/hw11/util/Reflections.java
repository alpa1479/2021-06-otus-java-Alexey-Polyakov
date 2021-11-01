package ru.otus.hw11.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Reflections {

    private Reflections() {
        throw new UnsupportedOperationException();
    }

    public static <T> T instantiate(Constructor<T> constructor) {
        return wrapException(constructor::newInstance);
    }

    public static <T> List<Object> getFieldValues(List<Field> fields, T object) {
        return wrapException(() -> {
            List<Object> fieldValues = new ArrayList<>();
            for (Field field : fields) {
                final Object fieldValue = getFieldValue(field, object);
                fieldValues.add(fieldValue);
            }
            return fieldValues;
        });
    }

    public static Object getFieldValue(Field field, Object object) {
        return wrapException(() -> {
            Object fieldValue = null;
            final int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                if (isNotAccessibleField(modifiers)) {
                    field.setAccessible(true);
                    fieldValue = field.get(object);
                }
            }
            return fieldValue;
        });
    }

    public static boolean isNotAccessibleField(int modifiers) {
        return Modifier.isPrivate(modifiers) || Modifier.isFinal(modifiers);
    }

    private static <T> T wrapException(Callable<T> action) {
        try {
            return action.call();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
