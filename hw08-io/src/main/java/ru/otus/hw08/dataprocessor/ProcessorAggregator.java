package ru.otus.hw08.dataprocessor;

import ru.otus.hw08.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ProcessorAggregator implements Processor {

    //группирует выходящий список по name, при этом суммирует поля value
    @Override
    public Map<String, Double> process(List<Measurement> data) {
        return data.stream()
                .collect(Collectors.groupingBy(Measurement::getName, TreeMap::new, Collectors.summingDouble(Measurement::getValue)));
    }
}
