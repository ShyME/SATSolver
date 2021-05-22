package pl.wat.tal.heuristic.alg;

import pl.wat.tal.DPLL.formula.Clause;
import pl.wat.tal.SatSolver;
import pl.wat.tal.heuristic.formula.ConjunctiveNormalFormula;
import pl.wat.tal.heuristic.formula.VariableData;
import pl.wat.tal.heuristic.input.DimacsStdInReader;
import pl.wat.tal.memoryCounter.MemoryCounter;

import java.util.*;
import java.util.stream.Collectors;

public class WalkSat implements SatSolver {

    private final MemoryCounter memoryCounter = MemoryCounter.getInstance();

    protected final long MAX_TRIES;
    protected final long MAX_FLIPS;
    protected final double WP;

    protected final Random random = new Random();
    protected Map<String, VariableData> variablesValues;

    public WalkSat(long MAX_TRIES, long MAX_FLIPS, double WP) {
        this.MAX_TRIES = MAX_TRIES;
        this.MAX_FLIPS = MAX_FLIPS;
        this.WP = WP;
    }

    @Override
    public boolean solve(List<Clause> clauses) {
        ConjunctiveNormalFormula cnf = ConjunctiveNormalFormula.createCNFFromClauses(clauses);
        return findSolution(cnf);
    }

    @Override
    public boolean solveFromFile(String filename) {
        DimacsStdInReader inReader = new DimacsStdInReader();
        ConjunctiveNormalFormula cnf = inReader.readFormulaFromFile(filename);
        return findSolution(cnf);
    }

    @Override
    public List<String> getResult() {
//        return variablesValues;
        return variablesValues.entrySet().stream()
                .map(x -> (x.getValue().isPositive() ? "" : "-") + x.getKey())
                .collect(Collectors.toList());
    }

    public boolean findSolution(ConjunctiveNormalFormula formula) {

        variablesValues = updateVariablesData(formula.getClauses());
        for (int i = 0; i < MAX_TRIES; i++) {
            generateRandomSolution(variablesValues);

            for (int j = 0; j < MAX_FLIPS; j++) {

                if (formula.isSatisfied(variablesValues)) {
                    return true;
                }
                String lastChangedVariable = findVariableToNegate(variablesValues, formula, WP).replace("-", "");
                variablesValues.get(lastChangedVariable).negate();
            }
        }
        return false;
    }

    private String findVariableToNegate(Map<String, VariableData> variablesValues, ConjunctiveNormalFormula formula, double p) {

        List<Clause> allClauses = formula.getClauses();
        List<Integer> unSatClauses = getUnSatClauses(variablesValues, formula.getClauses().size());

        int randomClause = unSatClauses.get(random.nextInt(unSatClauses.size()));
        Clause clause = allClauses.get(randomClause);

        int score;

        List<String> literalsWithScoreZero = new ArrayList<>();
        List<String> literalsWithHigherScore = new ArrayList<>();
        int minScore = Integer.MAX_VALUE, literalIdx = 0;

        for (int i = 0; i < clause.getLiterals().size(); i++) {
            String literal = clause.getLiterals().get(i);
            score = formula.getNumberOfUnSatClausesAfterChange(variablesValues, literal.replace("-", ""));

            if (score < minScore) {
                minScore = score;
                literalIdx = i;
            }

            if (score == 0) {
                literalsWithScoreZero.add(literal);
            } else if (score > 0) {
                literalsWithHigherScore.add(literal);
            }

        }

        if (literalsWithScoreZero.size() > 0) {
            return literalsWithScoreZero.get(random.nextInt(literalsWithScoreZero.size()));
        }

        if (random.nextDouble() <= p) {
            return literalsWithHigherScore.get(random.nextInt(literalsWithHigherScore.size()));
        }

        return literalsWithHigherScore.get(literalIdx);
    }

    protected void generateRandomSolution(Map<String, VariableData> variablesData) {
        variablesData.forEach((k, v) -> v.setValue(random.nextBoolean()));
    }

    protected Map<String, VariableData> updateVariablesData(List<Clause> clauses) {

        Map<String, VariableData> variablesValues = new HashMap<>();
        for (int i = 0; i < clauses.size(); i++) {
            List<String> literals = clauses.get(i).getLiterals();
            for (String literal : literals) {
                String variable = literal.replace("-", "");
                int finalI = i;
                variablesValues.computeIfAbsent(variable, k -> new VariableData(random.nextBoolean()));
                variablesValues.computeIfPresent(variable, (k, v) -> {
                    if (literal.contains("-")) {
                        v.addNegatedVarClauseIdx(finalI);
                    } else {
                        v.addVarClauseIdx(finalI);
                    }
                    return v;
                });
            }
        }

        return variablesValues;
    }

    protected List<Integer> getUnSatClauses(Map<String, VariableData> variablesData, int numberOfClauses) {
        Set<Integer> satClauses = getSetOfSatClauses(variablesData);

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < numberOfClauses; i++) {
            if (!satClauses.contains(i)) {
                result.add(i);
            }
        }
        return result;
    }

    protected Set<Integer> getSetOfSatClauses(Map<String, VariableData> variablesData) {
        Set<Integer> satClauses = new HashSet<>();
        for (Map.Entry<String, VariableData> entry : variablesData.entrySet()) {
            if (entry.getValue().isPositive()) {
                satClauses.addAll(entry.getValue().getVarClauses());
            } else {
                satClauses.addAll(entry.getValue().getNegatedVarClauses());
            }
        }

        return satClauses;
    }
}
