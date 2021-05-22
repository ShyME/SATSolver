package pl.wat.tal;

import pl.wat.tal.DPLL.formula.Clause;

import java.util.List;

public interface SatSolver {

    boolean solve(List<Clause> clauses);

    boolean solveFromFile(String filename);

    List<String> getResult();
}
