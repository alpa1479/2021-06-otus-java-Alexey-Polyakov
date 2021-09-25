package ru.otus.hw07.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw07.model.Message;
import ru.otus.hw07.processor.Processor;
import ru.otus.hw07.processor.homework.SwapProcessor;

public class SwapProcessorTest {

    Processor processor;

    @BeforeEach
    void setUp() {
        processor = new SwapProcessor();
    }

    @Test
    @DisplayName("Should swap values between field11 and field12 within Message")
    void shouldSwapField11AndField12Values() {
        // given
        final Message message = new Message.Builder(1).field11("field11").field12("field12").build();
        final Message swappedMessage = new Message.Builder(1).field11("field12").field12("field11").build();

        // when
        final Message processedMessage = processor.process(message);

        // then
        Assertions.assertThat(processedMessage).isEqualTo(swappedMessage);
    }
}