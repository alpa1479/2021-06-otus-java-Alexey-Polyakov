package ru.otus.hw12.crm.service;

import ru.otus.hw12.core.sessionmanager.DataBaseOperationException;

import javax.persistence.NoResultException;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final DbServiceAdmin dbServiceAdmin;

    public AuthenticationServiceImpl(DbServiceAdmin dbServiceAdmin) {
        this.dbServiceAdmin = dbServiceAdmin;
    }

    @Override
    public boolean authenticate(String login, String targetPassword) {
        try {
            return dbServiceAdmin.getAdminByLogin(login)
                    .map(admin -> admin.getPassword().equals(targetPassword))
                    .orElse(false);
        } catch (DataBaseOperationException e) {
            Throwable cause = e.getCause();
            if (cause instanceof NoResultException) {
                return false;
            }
            throw e;
        }
    }
}
