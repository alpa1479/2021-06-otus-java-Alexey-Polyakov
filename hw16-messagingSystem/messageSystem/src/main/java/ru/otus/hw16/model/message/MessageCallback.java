package ru.otus.hw16.model.message;

import ru.otus.hw16.model.resultdatatype.ResultDataType;

import java.util.function.Consumer;

public interface MessageCallback<T extends ResultDataType> extends Consumer<T> {
}
