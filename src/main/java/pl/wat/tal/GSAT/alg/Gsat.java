package pl.wat.tal.GSAT.alg;

import pl.wat.tal.DPLL.formula.Clause;
import pl.wat.tal.GSAT.formula.ConjunctiveNormalFormula;
import pl.wat.tal.GSAT.formula.VariableData;

import java.util.*;

public class Gsat {


    private final Random random;
    private String lastChangedVariable;
    private Map<String, VariableData> variablesValues;

    public Gsat() {
        this.random = new Random();
    }

    public Map<String, VariableData> findSolution(ConjunctiveNormalFormula formula, long maxTries, long maxFlips, double wp){

        variablesValues = updateVariablesData(formula.getClauses());
        for (int i = 0; i < maxTries; i++) {
            variablesValues = generateRandomSolution(variablesValues);

            for (int j = 0; j < maxFlips; j++) {

                if(formula.isSatisfied(variablesValues)){
                    System.out.println("Rozwiązanie znaleziono");
                    return variablesValues;
                }

                Strategy strategy = randStrategy(wp);
                if (strategy == Strategy.STANDARD){
                    lastChangedVariable = findVariableToNegate(variablesValues, formula);
                }else {
                    lastChangedVariable = findVariableToNegateRandomStep(variablesValues, formula);
                }
                variablesValues.get(lastChangedVariable).negate();
            }

        }

        System.out.println("Nie znaleziono satysfakcjonującego wartościowania");
        return null;
    }

    private String findVariableToNegateRandomStep(Map<String, VariableData> variablesValues, ConjunctiveNormalFormula formula){
        List<Integer> unSatClauses = getUnSatClauses(variablesValues, formula.getClauses().size());
        List<Clause> clauses = formula.getClauses();
        Clause randomlySelectedClause = clauses.get(unSatClauses.get(random.nextInt(unSatClauses.size())));
        List<String> literals = randomlySelectedClause.getLiterals();

        return literals.get(random.nextInt(literals.size())).replace("-", "");
    }

    private String findVariableToNegate(Map<String, VariableData> variablesValues, ConjunctiveNormalFormula formula){

        List<String> winList = new ArrayList<>();
        int highestDiff = Integer.MIN_VALUE;

        for (Map.Entry<String, VariableData> entry: variablesValues.entrySet()){

            String currentVariable = entry.getKey();
            if(entry.getKey().equals(lastChangedVariable)){
                continue;
            }

            int currentDiff = formula.getNumberOfSatClausesAfterChange(variablesValues, currentVariable)
                    - formula.getNumberOfUnSatClausesAfterChange(variablesValues, currentVariable);

            if(currentDiff > highestDiff){
                winList.clear();
                winList.add(entry.getKey());
                highestDiff = currentDiff;
            }else if(currentDiff == highestDiff){
                winList.add(entry.getKey());
                highestDiff = currentDiff;
            }
        }

        return winList.get(random.nextInt(winList.size()));
    }
    private Map<String, VariableData> generateRandomSolution(Map<String, VariableData> variablesData){
        variablesData.forEach((k, v) -> v.setValue(random.nextBoolean()));
        return variablesData;
    }

    private Map<String, VariableData> updateVariablesData(List<Clause> clauses){

        Map<String, VariableData> variablesValues = new HashMap<>();
        for (int i = 0; i < clauses.size(); i++) {
            List<String> literals = clauses.get(i).getLiterals();
            for (String literal: literals) {
                String variable = literal.replace("-", "");
                int finalI = i;
                variablesValues.computeIfAbsent(variable, k -> new VariableData(random.nextBoolean()));
                variablesValues.computeIfPresent(variable, (k, v) -> {
                    if(literal.contains("-")) {v.addNegatedVarClauseIdx(finalI);}
                    else {v.addVarClauseIdx(finalI);}
                    return v;
                });
            }
        }

        return variablesValues;
    }

    private Strategy randStrategy(double probabilityOfRandomStep){
        int prob = (int) (probabilityOfRandomStep * 100);
        Strategy[] strategies = new Strategy[100];

        for (int i = 0; i < strategies.length; i++) {
            if(i < prob){
                strategies[i] = Strategy.RANDOM_STEP;
            }else {
                strategies[i] = Strategy.STANDARD;
            }
        }

        return strategies[random.nextInt(strategies.length)];
    }

    private List<Integer> getUnSatClauses(Map<String, VariableData> variablesData, int numberOfClauses){
        Set<Integer> satClauses = getSetOfSatClauses(variablesData);

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < numberOfClauses; i++) {
            if (!satClauses.contains(i)){
                result.add(i);
            }
        }
        return result;
    }

    private Set<Integer> getSetOfSatClauses(Map<String, VariableData> variablesData){
        Set<Integer> satClauses = new HashSet<>();
        for(Map.Entry<String, VariableData> entry: variablesData.entrySet()){
            if(entry.getValue().isPositive()){
                satClauses.addAll(entry.getValue().getVarClauses());
            }else {
                satClauses.addAll(entry.getValue().getNegatedVarClauses());
            }
        }

        return satClauses;
    }
}
