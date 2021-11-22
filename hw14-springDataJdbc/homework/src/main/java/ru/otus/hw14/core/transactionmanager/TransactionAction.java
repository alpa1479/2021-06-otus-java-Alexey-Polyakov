package ru.otus.hw14.core.transactionmanager;

import java.util.function.Supplier;

public interface TransactionAction<T> extends Supplier<T> {
}
