package ru.otus.hw12.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw12.core.repository.DataTemplate;
import ru.otus.hw12.core.sessionmanager.TransactionManager;
import ru.otus.hw12.crm.model.Admin;

import java.util.Optional;

public class DbServiceAdminImpl implements DbServiceAdmin {

    private static final Logger log = LoggerFactory.getLogger(DbServiceAdminImpl.class);

    private final DataTemplate<Admin> adminDataTemplate;
    private final TransactionManager transactionManager;

    public DbServiceAdminImpl(TransactionManager transactionManager, DataTemplate<Admin> adminDataTemplate) {
        this.transactionManager = transactionManager;
        this.adminDataTemplate = adminDataTemplate;
    }

    @Override
    public Admin saveAdmin(Admin admin) {
        return transactionManager.doInTransaction(session -> {
            var adminCloned = admin.clone();
            if (admin.getId() == null) {
                adminDataTemplate.insert(session, adminCloned);
                log.info("created admin: {}", adminCloned);
                return adminCloned;
            }
            adminDataTemplate.update(session, adminCloned);
            log.info("updated admin: {}", adminCloned);
            return adminCloned;
        });
    }

    @Override
    public Optional<Admin> getAdminByLogin(String login) {
        return transactionManager.doInTransaction(session -> {
            var adminOptional = adminDataTemplate.findByAttributeName(session, "login", login);
            log.info("admin by login: {}", adminOptional);
            return adminOptional;
        });
    }
}
