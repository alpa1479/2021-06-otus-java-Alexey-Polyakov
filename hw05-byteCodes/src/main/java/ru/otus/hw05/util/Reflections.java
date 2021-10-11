package ru.otus.hw05.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Reflections {

    private Reflections() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Set<String> getSignaturesWithAnnotation(Object object, Class<? extends Annotation> annotation) {
        final Class<?> objectClass = object.getClass();
        final Method[] declaredMethods = objectClass.getDeclaredMethods();
        return Arrays.stream(declaredMethods)
                .filter(declaredMethod -> declaredMethod.isAnnotationPresent(annotation))
                .map(Reflections::toShortSignature)
                .collect(Collectors.toSet());
    }

    public static String toShortSignature(Method method) {
        StringJoiner joiner = new StringJoiner(",", method.getName() + "(", ")");
        for (Class<?> parameterType : method.getParameterTypes()) {
            joiner.add(parameterType.getTypeName());
        }
        return joiner.toString();
    }
}
