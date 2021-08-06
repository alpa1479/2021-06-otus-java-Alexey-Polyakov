package ru.otus.hw03;

import lombok.val;
import ru.otus.hw03.testplatform.printer.impl.ConsoleTestResultPrinter;
import ru.otus.hw03.testplatform.runner.TestRunner;

public class Main {

    public static void main(String[] args) {
        val testClassName = "ru.otus.hw03.testplatform.demo.DemoTest";
        val testResultPrinter = new ConsoleTestResultPrinter();
        val testRunner = new TestRunner(testResultPrinter);
        testRunner.run(testClassName);
    }
}
