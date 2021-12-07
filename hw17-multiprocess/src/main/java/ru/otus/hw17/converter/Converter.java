package ru.otus.hw17.converter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@FunctionalInterface
public interface Converter<V, T> {

    T convert(V value);

    default List<T> convert(List<V> values) {
        return values.stream().map(this::convert).toList();
    }

    default List<T> convert(Iterator<V> values) {
        List<T> list = new ArrayList<>();
        while (values.hasNext()) {
            V value = values.next();
            T convertedValue = convert(value);
            list.add(convertedValue);
        }
        return list;
    }
}
