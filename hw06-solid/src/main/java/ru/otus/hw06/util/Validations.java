package ru.otus.hw06.util;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Validations {

    private Validations() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static <T, E> void requireNonEmpty(Map<T, E> map, Supplier<? extends RuntimeException> exception) {
        test(map, Map::isEmpty, exception);
    }

    public static <T> void requireNonEmpty(Collection<T> collection, Supplier<? extends RuntimeException> exception) {
        test(collection, collectionToTest -> collectionToTest == null || collectionToTest.isEmpty(), exception);
    }

    public static <T> void requireNonNull(T object, Supplier<? extends RuntimeException> exception) {
        test(object, Objects::isNull, exception);
    }

    public static void requireNonNegative(int number, Supplier<? extends RuntimeException> exception) {
        test(number, numberToTest -> numberToTest < 0, exception);
    }

    private static <T> void test(T data, Predicate<T> predicate, Supplier<? extends RuntimeException> exception) {
        if (predicate.test(data)) {
            throw exception.get();
        }
    }
}
