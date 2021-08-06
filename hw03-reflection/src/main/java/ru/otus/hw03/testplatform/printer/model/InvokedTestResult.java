package ru.otus.hw03.testplatform.printer.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Method;
import java.util.Arrays;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvokedTestResult {

    boolean success;
    String className;
    String methodName;
    Throwable cause;

    public InvokedTestResult(Class<?> testClass, Method method, boolean success) {
        this.className = testClass.getSimpleName();
        this.methodName = method.getName();
        this.success = success;
    }

    public boolean isNotSuccess() {
        return !success;
    }

    @Override
    public String toString() {
        var invokedTestResultAsString = String.format("%s > %s - %s",
                className,
                methodName,
                success ? "PASSED" : "FAILED"
        );
        if (!success) {
            invokedTestResultAsString += String.format(" With message: %s %s",
                    cause.getMessage(),
                    Arrays.toString(cause.getStackTrace())
            );
        }
        return invokedTestResultAsString;
    }
}
