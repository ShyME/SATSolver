package pl.wat.tal.GSAT.alg;

import pl.wat.tal.DPLL.formula.Clause;
import pl.wat.tal.GSAT.formula.ConjunctiveNormalFormula;
import pl.wat.tal.GSAT.formula.VariableData;
import pl.wat.tal.MemoryCounter;

import java.util.*;

public class Gsat {


    private final Random random = new Random();
    private String lastChangedVariable;
    private Map<String, VariableData> variablesValues;

    public Map<String, VariableData> findSolution(ConjunctiveNormalFormula formula, long maxTries, long maxFlips, double wp){

        variablesValues = updateVariablesData(formula.getClauses());
        for (int i = 0; i < maxTries; i++) {
            variablesValues = generateRandomSolution(variablesValues);

            for (int j = 0; j < maxFlips; j++) {

                if(formula.isSatisfied(variablesValues)){
                    System.out.println("Rozwiązanie znaleziono");
                    return variablesValues;
                }
                lastChangedVariable = findVariableToNegate(variablesValues, formula, wp).replace("-", "");
                variablesValues.get(lastChangedVariable).negate();
            }
        }

        System.out.println("Nie znaleziono satysfakcjonującego wartościowania");
        return null;
    }

    private String findVariableToNegate(Map<String, VariableData> variablesValues, ConjunctiveNormalFormula formula, double p){

        List<Clause> allClauses = formula.getClauses();
        List<Integer> unSatClauses = getUnSatClauses(variablesValues, formula.getClauses().size());

        int randomClause = unSatClauses.get(random.nextInt(unSatClauses.size()));
        Clause clause = allClauses.get(randomClause);

        int score = 0;

        List<String> literalsWithScoreZero = new ArrayList<>();
        List<String> literalsWithHigherScore = new ArrayList<>();
        int minScore = Integer.MAX_VALUE, literalIdx = 0;

        for (int i = 0; i < clause.getLiterals().size(); i++) {
            String literal = clause.getLiterals().get(i);
            score = formula.getNumberOfUnSatClausesAfterChange(variablesValues, literal.replace("-", ""));

            if(score < minScore){
                minScore = score;
                literalIdx = i;
            }

            if(score == 0){
                literalsWithScoreZero.add(literal);
            }else if(score > 0){
                literalsWithHigherScore.add(literal);
            }

        }

        if(literalsWithScoreZero.size() > 0){
            return literalsWithScoreZero.get(random.nextInt(literalsWithScoreZero.size()));
        }

        if(random.nextDouble() <= p){
            return literalsWithHigherScore.get(random.nextInt(literalsWithHigherScore.size()));
        }

        return literalsWithHigherScore.get(literalIdx);
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
