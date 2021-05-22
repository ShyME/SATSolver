package pl.wat.tal.GSAT.formula;

import pl.wat.tal.DPLL.formula.Clause;
import pl.wat.tal.MemoryCounter;

import java.util.List;
import java.util.Map;

public class ConjunctiveNormalFormula {
    private final List<Clause> clauses;
    private final List<String> distinctLiterals;
    private final MemoryCounter memoryCounter = MemoryCounter.getInstance();

    public ConjunctiveNormalFormula(List<Clause> clauses, List<String> distinctLiterals) {
        this.clauses = clauses;
        this.distinctLiterals = distinctLiterals;

        distinctLiterals.forEach(memoryCounter::incrementStringCounter);
        clauses.forEach(clause -> clause.getLiterals().forEach(memoryCounter::incrementStringCounter));
    }

    public List<Clause> getClauses() {
        return clauses;
    }

    public List<String> getDistinctLiterals() {
        return distinctLiterals;
    }

    public boolean isSatisfied(Map<String, VariableData> variableValues) {
        for (Clause clause : clauses) {
            if (!clause.isClauseSatisfied(variableValues)) {
                return false;
            }
        }
        return true;
    }

    /*
       Jeżeli zmienna ma wartość True a jakieś klauzule w których jest zanegowana nie są obecnie spełnione, to trzeba je zliczyć.
       Przeciwnie dla False.
     */
    public int getNumberOfSatClausesAfterChange(Map<String, VariableData> currentResult, String variableToChange) {
        memoryCounter.incrementIntCounter(1);
        int counter = 0;
        if (currentResult.get(variableToChange).isPositive()) {
            for (Integer idx : currentResult.get(variableToChange).getNegatedVarClauses()) {
                if (!clauses.get(idx).isClauseSatisfied(currentResult)) {
                    counter++;
                }
            }
        } else {
            for (Integer idx : currentResult.get(variableToChange).getVarClauses()) {
                if (!clauses.get(idx).isClauseSatisfied(currentResult)) {
                    counter++;
                }
            }
        }
        return counter;
    }


    /*
        Jeżeli zmienna ma wartość True to trzeba sprawdzić czy klauzule w których jest ona niezanegowana zmienią się na
        niespełnione po jej negacji.
        Takie sytuacje trzeba zliczyć a wynik zwrócić
     */
    public int getNumberOfUnSatClausesAfterChange(Map<String, VariableData> currentResult, String variableToChange) {
        memoryCounter.incrementIntCounter(1);
        int counter = 0;
        if (currentResult.get(variableToChange).isPositive()) {
            for (Integer idx : currentResult.get(variableToChange).getVarClauses()) {
                if (clauses.get(idx).willBeUnSatAfterChange(currentResult, variableToChange)) {
                    counter++;
                }
            }
        } else {
            for (Integer idx : currentResult.get(variableToChange).getNegatedVarClauses()) {
                if (clauses.get(idx).willBeUnSatAfterChange(currentResult, variableToChange)) {
                    counter++;
                }
            }
        }
        return counter;
    }

}