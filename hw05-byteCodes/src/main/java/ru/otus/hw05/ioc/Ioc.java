package ru.otus.hw05.ioc;

import ru.otus.hw05.invocationhandler.LogInvocationHandler;
import ru.otus.hw05.logging.TestLogging;
import ru.otus.hw05.logging.TestLoggingImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Ioc {

    private Ioc() {
    }

    public static TestLogging createTestLoggingInstance() {
        final TestLogging testLogging = new TestLoggingImpl();
        InvocationHandler handler = new LogInvocationHandler(testLogging);
        return (TestLogging) Proxy.newProxyInstance(TestLogging.class.getClassLoader(), new Class<?>[] {TestLogging.class}, handler);
    }
}
