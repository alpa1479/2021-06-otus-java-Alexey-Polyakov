package ru.otus.hw07.processor;

import ru.otus.hw07.model.Message;

public interface Processor {

    Message process(Message message);

    //todo: 2. Сделать процессор, который поменяет местами значения field11 и field12

    //todo: 3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
    //         Секунда должна определяться во время выполнения.
    //         Тест - важная часть задания
}
