package ru.otus.hw07;

import ru.otus.hw07.handler.ComplexProcessor;
import ru.otus.hw07.listener.homework.HistoryListener;
import ru.otus.hw07.model.Message;
import ru.otus.hw07.model.ObjectForMessage;
import ru.otus.hw07.processor.homework.DateTimeProviderImpl;
import ru.otus.hw07.processor.homework.EvenSecondExceptionProcessor;
import ru.otus.hw07.processor.homework.SwapProcessor;

import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяться во время выполнения.
       4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
          Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
          Для него уже есть тест, убедитесь, что тест проходит
     */

    public static void main(String[] args) {
        var processors = List.of(
                new SwapProcessor(),
                new EvenSecondExceptionProcessor(new DateTimeProviderImpl())
        );

        var complexProcessor = new ComplexProcessor(processors, Throwable::printStackTrace);
        var historyListener = new HistoryListener();
        complexProcessor.addListener(historyListener);

        final ObjectForMessage objectForMessage = new ObjectForMessage();
        objectForMessage.setData(List.of("data1", "data3", "data3"));
        var message = new Message.Builder(1L)
                .field11("field11")
                .field12("field12")
                .field13(objectForMessage)
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(historyListener);
    }
}
