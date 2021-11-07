package ru.otus.hw12.crm.service;

import ru.otus.hw12.crm.model.Admin;

import java.util.Optional;

public interface DbServiceAdmin {

    Admin saveAdmin(Admin admin);

    Optional<Admin> getAdminByLogin(String login);
}
