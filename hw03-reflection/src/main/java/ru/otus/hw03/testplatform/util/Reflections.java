package ru.otus.hw03.testplatform.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class Reflections {

    @SneakyThrows
    public Class<?> getClass(String className) {
        return Class.forName(className);
    }

    public Optional<Method> getMethodWithAnnotation(Class<?> targetClass, Class<? extends Annotation> annotation) {
        return Arrays.stream(targetClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .findFirst();
    }

    public List<Method> getMethodsWithAnnotation(Class<?> targetClass, Class<? extends Annotation> annotation) {
        return Arrays.stream(targetClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public <T> T instantiate(Class<T> targetClass) {
        return targetClass.getDeclaredConstructor().newInstance();
    }

    @SneakyThrows
    public void callMethod(Object object, Method method, Object... args) {
        method.setAccessible(true);
        method.invoke(object, args);
    }
}
