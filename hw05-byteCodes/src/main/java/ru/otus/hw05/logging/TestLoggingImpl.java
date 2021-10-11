package ru.otus.hw05.logging;

import ru.otus.hw05.annotation.Log;

public class TestLoggingImpl implements TestLogging {

    @Log
    @Override
    public void calculation(int param1) {

    }

    @Log
    @Override
    public void calculation(int param1, int param2) {

    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {

    }

    // without @Log annotation to check that logging works only for annotated methods
    @Override
    public void calculation(int param1, int param2, String param3, String param4) {

    }
}
