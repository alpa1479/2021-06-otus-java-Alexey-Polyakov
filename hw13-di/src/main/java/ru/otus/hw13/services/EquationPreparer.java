package ru.otus.hw13.services;

import ru.otus.hw13.model.Equation;

import java.util.List;

public interface EquationPreparer {

    List<Equation> prepareEquationsFor(int base);
}
