package ru.otus.hw03.testplatform.printer.impl;

import ru.otus.hw03.testplatform.printer.api.TestResultPrinter;
import ru.otus.hw03.testplatform.printer.model.TestsResultInformation;

public class ConsoleTestResultPrinter implements TestResultPrinter {

    @Override
    public void printResults(TestsResultInformation testsResultInformation) {
        System.out.println(testsResultInformation);
    }
}
