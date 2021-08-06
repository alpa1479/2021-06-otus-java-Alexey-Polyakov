package ru.otus.hw03.testplatform.runner;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import ru.otus.hw03.testplatform.annotations.After;
import ru.otus.hw03.testplatform.annotations.Before;
import ru.otus.hw03.testplatform.annotations.Test;
import ru.otus.hw03.testplatform.printer.api.TestResultPrinter;
import ru.otus.hw03.testplatform.printer.model.InvokedTestResult;
import ru.otus.hw03.testplatform.printer.model.TestsResultInformation;
import ru.otus.hw03.testplatform.util.Reflections;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class TestRunner {

    TestResultPrinter testResultPrinter;

    public void run(String testClassName) {
        val testClass = Reflections.getClass(testClassName);

        val beforeMethod = Reflections.getMethodWithAnnotation(testClass, Before.class);
        val testMethods = Reflections.getMethodsWithAnnotation(testClass, Test.class);
        val afterMethod = Reflections.getMethodWithAnnotation(testClass, After.class);

        List<InvokedTestResult> invokedTestResults = invokeTests(testClass, testMethods, beforeMethod, afterMethod);
        TestsResultInformation testsResultInformation = new TestsResultInformation(invokedTestResults);
        testResultPrinter.printResults(testsResultInformation);
    }

    private List<InvokedTestResult> invokeTests(Class<?> testClass, List<Method> testMethods,
                                                Optional<Method> beforeMethod, Optional<Method> afterMethod) {
        return testMethods.stream()
                .map(testMethod -> invokeTest(testClass, testMethod, beforeMethod, afterMethod))
                .collect(Collectors.toList());
    }

    private InvokedTestResult invokeTest(Class<?> testClass, Method testMethod,
                                         Optional<Method> beforeMethod, Optional<Method> afterMethod) {
        val testClassInstance = Reflections.instantiate(testClass);
        val testResult = new InvokedTestResult(testClass, testMethod, true);
        try {
            beforeMethod.ifPresent(before -> Reflections.callMethod(testClassInstance, before));
            Reflections.callMethod(testClassInstance, testMethod);
        } catch (Exception e) {
            testResult.setCause(e.getCause());
            testResult.setSuccess(false);
        } finally {
            afterMethod.ifPresent(after -> Reflections.callMethod(testClassInstance, after));
        }
        return testResult;
    }
}
