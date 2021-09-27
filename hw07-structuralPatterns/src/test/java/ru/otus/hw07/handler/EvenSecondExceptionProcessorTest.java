package ru.otus.hw07.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw07.handler.mocks.MockDateTimeProviderImpl;
import ru.otus.hw07.model.Message;
import ru.otus.hw07.processor.Processor;
import ru.otus.hw07.processor.homework.EvenSecondException;
import ru.otus.hw07.processor.homework.EvenSecondExceptionProcessor;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

public class EvenSecondExceptionProcessorTest {

    @Test
    @DisplayName("Should throw exception in even second")
    void shouldThrowExceptionInEvenSecond() {
        // given
        final Message emptyMessage = new Message.Builder(1).build();
        final Processor processor = new EvenSecondExceptionProcessor(new MockDateTimeProviderImpl());

        // when
        final Throwable thrown = catchThrowable(() -> processor.process(emptyMessage));

        // then
        Assertions.assertThat(thrown).isInstanceOf(EvenSecondException.class);
    }
}
