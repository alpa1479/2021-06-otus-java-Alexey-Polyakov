package ru.otus.hw01;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HelloOtus {
    public static void main(String... args) {
        List<Integer> example = IntStream.range(0, 100).boxed().collect(Collectors.toList());
        System.out.println(Lists.reverse(example));
    }
}
