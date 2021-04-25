package pl.wat.tal.satsolver;

import pl.wat.tal.formula.ConjunctiveNormalFormula;

import java.util.Map;

public interface SATSolver {
    boolean solve(ConjunctiveNormalFormula cnf, Map<Integer, Boolean> result);
    boolean solve2(ConjunctiveNormalFormula cnf, Map<Integer, Boolean> result);
}
