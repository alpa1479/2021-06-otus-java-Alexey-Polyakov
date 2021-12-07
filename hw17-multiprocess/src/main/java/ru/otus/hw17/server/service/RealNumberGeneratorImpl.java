package ru.otus.hw17.server.service;

import ru.otus.hw17.model.GeneratedNumber;
import ru.otus.hw17.model.Range;

import java.util.List;
import java.util.stream.IntStream;

public class RealNumberGeneratorImpl implements RealNumberGenerator {

    @Override
    public List<GeneratedNumber> generateNumberSequence(Range range) {
        return IntStream.rangeClosed(range.firstValue(), range.lastValue())
                .mapToObj(GeneratedNumber::new)
                .toList();
    }
}
