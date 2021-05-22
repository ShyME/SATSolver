package pl.wat.tal.generator;

import pl.wat.tal.DPLL.formula.Clause;
import pl.wat.tal.DPLL.satsolver.DPLLSatSolver;
import pl.wat.tal.SatSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    public List<Clause> generate(int numberOfVariables, int numberOfClauses) {
        List<Clause> clauses = new ArrayList<>();
        boolean formulaIsSolvable = false;
        while (!formulaIsSolvable) {
            clauses = new ArrayList<>();
            for (int i = 0; i < numberOfClauses; i++) {
                clauses.add(new Clause(generateLiterals(numberOfVariables)));
            }

            long start = System.currentTimeMillis();
            formulaIsSolvable = checkWithDpll(clauses, numberOfVariables, numberOfClauses);
            if (System.currentTimeMillis() - start < 1200) {
                formulaIsSolvable = false;
            }
        }
        return clauses;
    }

    private boolean checkWithDpll(List<Clause> clauses, int numberOfVariables, int numberOfClauses) {
        SatSolver satSolver = new DPLLSatSolver(numberOfVariables, numberOfClauses);
        return satSolver.solve(clauses);
    }

    private static List<String> generateLiterals(int numberOfVariables) {
        Random random = new Random();
        List<String> literals = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            literals.add((random.nextBoolean() ? "" : "-")
                    + random.nextInt(numberOfVariables) + 1);
        }
        return literals;
    }
}
