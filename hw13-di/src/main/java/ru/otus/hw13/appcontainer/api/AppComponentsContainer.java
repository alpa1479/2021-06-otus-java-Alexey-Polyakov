package ru.otus.hw13.appcontainer.api;

public interface AppComponentsContainer {

    <C> C getAppComponent(Class<C> componentClass);

    <C> C getAppComponent(String componentName);
}
