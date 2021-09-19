package ru.otus.hw08.dataprocessor;

import ru.otus.hw08.model.Measurement;

import java.util.List;

public interface Loader {

    List<Measurement> load();
}
