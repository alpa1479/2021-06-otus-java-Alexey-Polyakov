package ru.otus.hw05;

import ru.otus.hw05.ioc.Ioc;
import ru.otus.hw05.logging.TestLogging;

public class Demo {

    public static void main(String[] args) {
        TestLogging testLogging = Ioc.createTestLoggingInstance();

        int param1 = 1;
        int param2 = 2;
        String param3 = "3";
        String param4 = "4";

        testLogging.calculation(param1);
        testLogging.calculation(param1, param2);
        testLogging.calculation(param1, param2, param3);
        testLogging.calculation(param1, param2, param3, param4);
    }
}
