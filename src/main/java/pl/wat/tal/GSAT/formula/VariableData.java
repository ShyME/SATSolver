package pl.wat.tal.GSAT.formula;

import java.util.ArrayList;
import java.util.List;

public class VariableData {
    private boolean value;
    private List<Integer> varClauses;
    private List<Integer> negatedVarClauses;

    public VariableData(boolean value) {
        this.value = value;
        this.varClauses = new ArrayList<>();
        this.negatedVarClauses = new ArrayList<>();
    }

    public void setValue(boolean value){
        this.value = value;
    }

    public void negate() {
        this.value = !this.value;
    }

    public boolean isPositive() {
        return value;
    }

    public List<Integer> getVarClauses() {
        return varClauses;
    }

    public List<Integer> getNegatedVarClauses() {
        return negatedVarClauses;
    }

    public void addVarClauseIdx(int clauseIdx){
        varClauses.add(clauseIdx);
    }

    public void addNegatedVarClauseIdx(int clauseIdx){
        negatedVarClauses.add(clauseIdx);
    }

    @Override
    public String toString() {
        return "VariableData{" +
                "value=" + value +
                '}';
    }
}
