package ru.otus.hw12.crm.service;

public interface AuthenticationService {

    boolean authenticate(String login, String password);
}
