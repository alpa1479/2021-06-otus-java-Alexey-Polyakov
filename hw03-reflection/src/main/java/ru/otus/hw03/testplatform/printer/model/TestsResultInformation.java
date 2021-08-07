package ru.otus.hw03.testplatform.printer.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.val;

import java.util.List;
import java.util.stream.Collectors;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestsResultInformation {

    List<InvokedTestResult> invokedTestResults;

    public long getAmountOfTests() {
        return invokedTestResults.size();
    }

    public long getAmountOfPassedTests() {
        return invokedTestResults.stream().filter(InvokedTestResult::isSuccess).count();
    }

    public long getAmountOfFailedTests() {
        return invokedTestResults.stream().filter(InvokedTestResult::isNotSuccess).count();
    }

    private String getInvokedTestResultsAsString() {
        return invokedTestResults.stream()
                .map(InvokedTestResult::toString)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String toString() {
        val testsResultStartMessage = "====================================== Test Results START ====================================\n";
        val invokedTestResultsAsString = getInvokedTestResultsAsString();
        val testsResultEndMessage = String.format("""
                \nStatistic:
                    amount of passed tests: %d
                    amount of failed tests: %d
                    amount of tests: %d
                ====================================== Test Results END ======================================
                """, getAmountOfPassedTests(), getAmountOfFailedTests(), getAmountOfTests());
        return testsResultStartMessage + invokedTestResultsAsString + testsResultEndMessage;
    }
}
