package pl.wat.tal.satsolver;

import pl.wat.tal.formula.ConjunctiveNormalFormula;

import java.util.List;

public interface SatSolver {
    boolean solve(ConjunctiveNormalFormula cnf);
    List<String> getTrueLiterals();
}
