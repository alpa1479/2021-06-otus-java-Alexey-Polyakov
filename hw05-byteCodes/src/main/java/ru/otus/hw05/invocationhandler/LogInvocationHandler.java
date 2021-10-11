package ru.otus.hw05.invocationhandler;

import ru.otus.hw05.annotation.Log;
import ru.otus.hw05.util.Reflections;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class LogInvocationHandler implements InvocationHandler {

    private final Object object;
    private final Set<String> logMethodSignatures;

    public LogInvocationHandler(Object object) {
        this.object = object;
        this.logMethodSignatures = Reflections.getSignaturesWithAnnotation(object, Log.class);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (hasLogMethodSignature(method)) {
            logMethodNameWithParams(method, args);
        }
        return method.invoke(object, args);
    }

    private boolean hasLogMethodSignature(Method method) {
        return logMethodSignatures.contains(Reflections.toShortSignature(method));
    }

    private void logMethodNameWithParams(Method method, Object[] args) {
        String params = Arrays.stream(args).map(Object::toString).collect(Collectors.joining(", "));
        System.out.printf("executed method: %s, params: %s%n", method.getName(), params);
    }
}
