package pl.wat.tal.DPLL.formula;

import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.ArrayList;

@ToString
@Getter
public class NewClause {
    private List<String> activeLiterals;
    private List<String> removedLiterals;

    public NewClause(List<String> literals) {
        this.activeLiterals = literals;
        this.removedLiterals = new ArrayList<>();
    }

    public boolean isEmpty() {
        return activeLiterals.isEmpty();
    }

    public boolean isUnitClause() {
        return activeLiterals.size() == 1;
    }

    public boolean contains(String literal) {
        return activeLiterals.contains(literal);
    }
    public boolean contained(String literal) {
        return removedLiterals.contains(literal);
    }

    public void remove(String literal) {
        if(activeLiterals.contains(literal)) {
            activeLiterals.remove(literal);
            removedLiterals.add(literal);
        }
    }

    public void backtrack(String literal) {
        if(removedLiterals.contains(literal)) {
            removedLiterals.remove(literal);
            activeLiterals.add(literal);
        }
    }
}
