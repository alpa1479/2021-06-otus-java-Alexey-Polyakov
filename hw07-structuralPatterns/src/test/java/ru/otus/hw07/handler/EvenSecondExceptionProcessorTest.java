package ru.otus.hw07.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw07.model.Message;
import ru.otus.hw07.processor.Processor;
import ru.otus.hw07.processor.homework.DateTimeProvider;
import ru.otus.hw07.processor.homework.EvenSecondException;
import ru.otus.hw07.processor.homework.EvenSecondExceptionProcessor;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EvenSecondExceptionProcessorTest {

    @Test
    @DisplayName("Should throw exception in even second")
    void shouldThrowExceptionInEvenSecond() {
        // given
        final Message emptyMessage = new Message.Builder(1).build();
        final DateTimeProvider dateTimeProvider = mock(DateTimeProvider.class);
        final LocalDateTime constantTime = LocalDateTime.of(2021, 1, 1, 1, 1, 2);
        when(dateTimeProvider.getDate()).thenReturn(constantTime);
        final Processor processor = new EvenSecondExceptionProcessor(dateTimeProvider);

        // when
        final Throwable thrown = catchThrowable(() -> processor.process(emptyMessage));

        // then
        Assertions.assertThat(thrown).isInstanceOf(EvenSecondException.class);
    }
}
