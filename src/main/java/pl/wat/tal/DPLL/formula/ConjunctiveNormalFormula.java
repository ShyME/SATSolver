package pl.wat.tal.DPLL.formula;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class ConjunctiveNormalFormula {
    private List<Clause> clauses;
    private int variableNumber;
    private int clauseNumber;

    public ConjunctiveNormalFormula(int variableNumber, int clauseNumber, List<Clause> clauses) {
        this.variableNumber = variableNumber;
        this.clauseNumber = clauseNumber;
        this.clauses = clauses;
    }

    public void removeContainingClauses(String literal) {
        clauses.removeIf(clause -> clause.contains(literal));
    }

    public void removeLiteralFromClauses(String literal) {
        clauses.forEach(c -> c.remove(literal));
    }

    public boolean isEmpty() {
        return clauses.isEmpty();
    }

    public ConjunctiveNormalFormula copy() {
        return new ConjunctiveNormalFormula(variableNumber, clauseNumber, getClausesDeepCopy());
    }

    public boolean containsAnyEmptyClause() {
        for(Clause clause : clauses) {
            if(clause.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private List<Clause> getClausesDeepCopy() {
        List<Clause> clausesDeepCopy = new ArrayList<>();
        for(Clause clause : clauses) {
            clausesDeepCopy.add(clause.getDeepCopy());
        }
        return clausesDeepCopy;
    }
}
