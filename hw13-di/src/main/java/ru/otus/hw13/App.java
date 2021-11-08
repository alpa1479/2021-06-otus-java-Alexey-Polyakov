package ru.otus.hw13;

import ru.otus.hw13.appcontainer.AppComponentsContainerImpl;
import ru.otus.hw13.appcontainer.api.AppComponentsContainer;
import ru.otus.hw13.config.AppConfig1;
import ru.otus.hw13.config.AppConfig2;
import ru.otus.hw13.services.GameProcessor;

/*
В классе AppComponentsContainerImpl реализовать обработку, полученной в конструкторе конфигурации,
основываясь на разметке аннотациями из пакета appcontainer. Так же необходимо реализовать методы getAppComponent.
В итоге должно получиться работающее приложение. Менять можно только класс AppComponentsContainerImpl.

Раскоментируйте тест:
@Disabled //надо удалить

PS Приложение представляет из себя тренажер таблицы умножения)
*/

public class App {

    public static void main(String[] args) throws Exception {
        // Опциональные варианты
        AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig1.class, AppConfig2.class);

        // Тут можно использовать библиотеку Reflections (см. зависимости)
        //AppComponentsContainer container = new AppComponentsContainerImpl("ru.otus.hw13.config");

        // Обязательный вариант
//        AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig1.class);

        // Приложение должно работать в каждом из указанных ниже вариантов
//        GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);
//        GameProcessor gameProcessor = container.getAppComponent(GameProcessorImpl.class);
        GameProcessor gameProcessor = container.getAppComponent("gameProcessor");

        gameProcessor.startGame();
    }
}
