package ru.otus.hw13.config;

import ru.otus.hw13.appcontainer.api.AppComponent;
import ru.otus.hw13.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.hw13.services.EquationPreparer;
import ru.otus.hw13.services.EquationPreparerImpl;
import ru.otus.hw13.services.IOService;
import ru.otus.hw13.services.IOServiceConsole;

@AppComponentsContainerConfig(order = 0)
public class AppConfig1 {

    @AppComponent(order = 0, name = "equationPreparer")
    public EquationPreparer equationPreparer() {
        return new EquationPreparerImpl();
    }

    @AppComponent(order = 1, name = "ioService")
    public IOService ioService() {
        return new IOServiceConsole(System.out, System.in);
    }
}
