package pl.wat.tal.formula;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class Clause {
    private List<String> literals;

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

    public boolean equals(Clause clause) {
        return clause.getLiterals().containsAll(literals) && literals.containsAll(clause.getLiterals());
    }
}
