package pl.wat.tal.DPLL.satsolver;

import pl.wat.tal.DPLL.formula.ConjunctiveNormalFormula;

import java.util.List;

public interface SatSolver {
    boolean solve(ConjunctiveNormalFormula cnf);
    List<String> getTrueLiterals();
}
