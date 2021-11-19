package ru.otus.hw13;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.otus.hw13.appcontainer.AppComponentsContainerImpl;
import ru.otus.hw13.services.EquationPreparer;
import ru.otus.hw13.services.IOService;
import ru.otus.hw13.services.PlayerService;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    @DisplayName("Из контекста тремя способами должен корректно доставаться компонент с проставленными полями")
    @ParameterizedTest(name = "Достаем по: {0}")
    @CsvSource(value = {"GameProcessor, ru.otus.hw13.services.GameProcessor",
            "GameProcessorImpl, ru.otus.hw13.services.GameProcessor",
            "gameProcessor, ru.otus.hw13.services.GameProcessor",

            "IOService, ru.otus.hw13.services.IOService",
            "IOServiceConsole, ru.otus.hw13.services.IOService",
            "ioService, ru.otus.hw13.services.IOService",

            "PlayerService, ru.otus.hw13.services.PlayerService",
            "PlayerServiceImpl, ru.otus.hw13.services.PlayerService",
            "playerService, ru.otus.hw13.services.PlayerService",

            "EquationPreparer, ru.otus.hw13.services.EquationPreparer",
            "EquationPreparerImpl, ru.otus.hw13.services.EquationPreparer",
            "equationPreparer, ru.otus.hw13.services.EquationPreparer"
    })
    void shouldExtractFromContextCorrectComponentWithNotNullFields(String classNameOrBeanId, Class<?> rootClass) throws Exception {
        var ctx = new AppComponentsContainerImpl("ru.otus.hw13.config");

        assertThat(classNameOrBeanId).isNotEmpty();
        Object component;
        if (classNameOrBeanId.charAt(0) == classNameOrBeanId.toUpperCase().charAt(0)) {
            Class<?> gameProcessorClass = Class.forName("ru.otus.hw13.services." + classNameOrBeanId);
            assertThat(rootClass).isAssignableFrom(gameProcessorClass);

            component = ctx.getAppComponent(gameProcessorClass);
        } else {
            component = ctx.getAppComponent(classNameOrBeanId);
        }
        assertThat(component).isNotNull();
        assertThat(rootClass).isAssignableFrom(component.getClass());

        for (var field : component.getClass().getFields()) {
            var fieldValue = field.get(component);
            assertThat(fieldValue).isNotNull().isInstanceOfAny(IOService.class, PlayerService.class, EquationPreparer.class);
        }

    }
}