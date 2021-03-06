package pl.wat.tal.DPLL.formula;

import lombok.Getter;
import lombok.ToString;
import pl.wat.tal.heuristic.formula.VariableData;
import pl.wat.tal.memoryCounter.ComplexityCounter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ToString
@Getter
public class Clause {

    private ComplexityCounter complexityCounter = ComplexityCounter.getInstance();

    private final List<String> literals;

    public Clause(List<String> literals) {
        this.literals = literals;
    }

    public boolean isEmpty() {
        return literals.isEmpty();
    }

    public boolean isUnitClause() {
        return literals.size() == 1;
    }

    public boolean contains(String literal) {
        return literals.contains(literal);
    }

    public void remove(String literal) {
        literals.remove(literal);
    }

    public Clause getDeepCopy() {
        List<String> newLiterals = new ArrayList<>(literals);
        return new Clause(newLiterals);
    }

    public boolean isClauseSatisfied(Map<String, VariableData> variableValues){
        for(String literal: literals){
            complexityCounter.incrementOperationCounter(1);
            String variable = literal.replace("-", "");
            if((literal.contains("-") && !variableValues.get(variable).isPositive())
                    || (!literal.contains("-") && variableValues.get(variable).isPositive())){
                return true;
            }
        }
        return false;
    }

    public boolean willBeUnSatAfterChange(Map<String, VariableData> variableValues, String variable){
        variableValues.get(variable).negate();
        boolean result = isClauseSatisfied(variableValues);
        variableValues.get(variable).negate();

        return !result;
    }
}
