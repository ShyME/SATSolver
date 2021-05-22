package pl.wat.tal.GSAT.input;

import pl.wat.tal.DPLL.formula.Clause;

import java.util.List;

public interface DataGenerator {
    List<Clause> generate(int numberOfVariables, int numberOfClauses) throws InterruptedException;
}
