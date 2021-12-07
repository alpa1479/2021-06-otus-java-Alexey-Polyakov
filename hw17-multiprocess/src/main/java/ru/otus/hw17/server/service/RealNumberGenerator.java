package ru.otus.hw17.server.service;

import ru.otus.hw17.model.GeneratedNumber;
import ru.otus.hw17.model.Range;

import java.util.List;

public interface RealNumberGenerator {

    List<GeneratedNumber> generateNumberSequence(Range range);
}
