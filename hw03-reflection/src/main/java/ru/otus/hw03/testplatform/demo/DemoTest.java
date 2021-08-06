package ru.otus.hw03.testplatform.demo;

import org.assertj.core.api.Assertions;
import ru.otus.hw03.testplatform.annotations.After;
import ru.otus.hw03.testplatform.annotations.Before;
import ru.otus.hw03.testplatform.annotations.Test;

public class DemoTest {

    String stringAsObject;
    String stringAsLiteral;

    @Before
    void setUp() {
        stringAsObject = new String("Test string");
        stringAsLiteral = "Test string";
    }

    @Test
    void shouldCheckThatStringReferencesAreEquals() {
        boolean equals = stringAsLiteral == stringAsObject;
        Assertions.assertThat(equals).isTrue();
    }

    @Test
    void shouldCheckThatStringsAreEquals() {
        Assertions.assertThat(stringAsLiteral).isEqualTo(stringAsObject);
    }

    @After
    void tearDown() {
        stringAsObject = null;
        stringAsLiteral = null;
    }
}
