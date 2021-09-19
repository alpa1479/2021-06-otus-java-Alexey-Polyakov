package ru.otus.hw08.dataprocessor;

import ru.otus.hw08.model.Measurement;

import java.util.List;
import java.util.Map;

public interface Processor {

    Map<String, Double> process(List<Measurement> data);
}
