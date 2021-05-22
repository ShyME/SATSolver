package pl.wat.tal.heuristic.alg;


import pl.wat.tal.DPLL.formula.Clause;
import pl.wat.tal.heuristic.formula.ConjunctiveNormalFormula;
import pl.wat.tal.heuristic.formula.VariableData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GSat extends WalkSat {

    private String lastChangedVariable;

    public GSat(long MAX_TRIES, long MAX_FLIPS, double WP) {
        super(MAX_TRIES, MAX_FLIPS, WP);
    }

    @Override
    public boolean findSolution(ConjunctiveNormalFormula formula) {

        variablesValues = updateVariablesData(formula.getClauses());
        for (int i = 0; i < MAX_TRIES; i++) {
            generateRandomSolution(variablesValues);

            for (int j = 0; j < MAX_FLIPS; j++) {

                if (formula.isSatisfied(variablesValues)) {
                    return true;
                }

                Strategy strategy = randStrategy(WP);
                if (strategy == Strategy.STANDARD) {
                    lastChangedVariable = findVariableToNegate(variablesValues, formula);
                } else {
                    lastChangedVariable = findVariableToNegateRandomStep(variablesValues, formula);
                }
                variablesValues.get(lastChangedVariable).negate();
            }

        }
        return false;
    }

    private String findVariableToNegateRandomStep(Map<String, VariableData> variablesValues, ConjunctiveNormalFormula formula) {
        List<Integer> unSatClauses = getUnSatClauses(variablesValues, formula.getClauses().size());
        List<Clause> clauses = formula.getClauses();
        Clause randomlySelectedClause = clauses.get(unSatClauses.get(random.nextInt(unSatClauses.size())));
        List<String> literals = randomlySelectedClause.getLiterals();

        return literals.get(random.nextInt(literals.size())).replace("-", "");
    }

    private String findVariableToNegate(Map<String, VariableData> variablesValues, ConjunctiveNormalFormula formula) {

        List<String> winList = new ArrayList<>();
        int highestDiff = Integer.MIN_VALUE;

        for (Map.Entry<String, VariableData> entry : variablesValues.entrySet()) {

            String currentVariable = entry.getKey();
            if (entry.getKey().equals(lastChangedVariable)) {
                continue;
            }

            int currentDiff = formula.getNumberOfSatClausesAfterChange(variablesValues, currentVariable)
                    - formula.getNumberOfUnSatClausesAfterChange(variablesValues, currentVariable);

            if (currentDiff > highestDiff) {
                winList.clear();
                winList.add(entry.getKey());
                highestDiff = currentDiff;
            } else if (currentDiff == highestDiff) {
                winList.add(entry.getKey());
                highestDiff = currentDiff;
            }
        }

        return winList.get(random.nextInt(winList.size()));
    }

    private Strategy randStrategy(double probabilityOfRandomStep) {
        int prob = (int) (probabilityOfRandomStep * 100);
        Strategy[] strategies = new Strategy[100];

        for (int i = 0; i < strategies.length; i++) {
            if (i < prob) {
                strategies[i] = Strategy.RANDOM_STEP;
            } else {
                strategies[i] = Strategy.STANDARD;
            }
        }

        return strategies[random.nextInt(strategies.length)];
    }
}