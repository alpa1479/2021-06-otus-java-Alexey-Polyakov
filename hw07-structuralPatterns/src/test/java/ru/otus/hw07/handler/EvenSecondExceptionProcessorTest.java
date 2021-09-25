package ru.otus.hw07.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import ru.otus.hw07.model.Message;
import ru.otus.hw07.processor.Processor;
import ru.otus.hw07.processor.homework.EvenSecondException;
import ru.otus.hw07.processor.homework.EvenSecondExceptionProcessor;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.mockStatic;

public class EvenSecondExceptionProcessorTest {

    Processor processor;

    @BeforeEach
    void setUp() {
        processor = new EvenSecondExceptionProcessor();
    }

    @Test
    @DisplayName("Should throw exception in even second")
    void shouldThrowExceptionInEvenSecond() {
        final Instant now = Instant.now(Clock.fixed(Instant.parse("2021-01-01T01:01:02Z"), ZoneOffset.UTC));
        try (MockedStatic<Instant> mocked = mockStatic(Instant.class)) {

            // given
            mocked.when(Instant::now).thenReturn(now);
            final Message emptyMessage = new Message.Builder(1).build();

            // when
            Throwable thrown = catchThrowable(() -> processor.process(emptyMessage));

            // then
            Assertions.assertThat(thrown).isInstanceOf(EvenSecondException.class);
        }
    }
}
