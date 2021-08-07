package ru.otus.hw03.testplatform.printer.api;

import ru.otus.hw03.testplatform.printer.model.TestsResultInformation;

public interface TestResultPrinter {

    void printResults(TestsResultInformation testsResultInformation);
}
