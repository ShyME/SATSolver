package pl.wat.tal.GSAT.formula;

import java.util.List;
import java.util.Map;

public class Clause {
    private List<String> literals;

    public Clause(List<String> literals) {
        this.literals = literals;
    }

    public List<String> getLiterals() {
        return literals;
    }

    public boolean isClauseSatisfied(Map<String, VariableData> variableValues){
        for(String literal: literals){
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