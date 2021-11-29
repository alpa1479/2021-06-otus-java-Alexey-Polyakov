package ru.otus.hw16.core.transactionmanager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionManagerSpring implements TransactionManager {

    @Override
    @Transactional
    public <T> T doInTransaction(TransactionAction<T> action) {
        return action.get();
    }

    @Override
    @Transactional(readOnly = true)
    public <T> T doInReadOnlyTransaction(TransactionAction<T> action) {
        return action.get();
    }
}
