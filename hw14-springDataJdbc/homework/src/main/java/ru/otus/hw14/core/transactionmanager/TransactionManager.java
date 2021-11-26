package ru.otus.hw14.core.transactionmanager;

public interface TransactionManager {

    <T> T doInTransaction(TransactionAction<T> action);
}
