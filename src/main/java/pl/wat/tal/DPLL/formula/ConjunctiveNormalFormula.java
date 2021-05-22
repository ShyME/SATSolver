package pl.wat.tal.DPLL.formula;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class ConjunctiveNormalFormula {
    private final List<Clause> clauses;
    private final int variableNumber;
    private final int clauseNumber;

    public ConjunctiveNormalFormula(int variableNumber, int clauseNumber, List<Clause> clauses) {
        this.variableNumber = variableNumber;
        this.clauseNumber = clauseNumber;
        this.clauses = clauses;
    }

    public static boolean checkSAT(List<Clause> clauses, List<String> result) {
        List<Clause> clausesCopy = new ArrayList<>(clauses);
        for (int i = 0; i < clausesCopy.size(); i++) {
            Clause clause = clausesCopy.get(i);
            for (String literal : result) {
                if (clause.contains(literal)) {
                    clausesCopy.removeIf(c -> c.contains(literal));
                    i--;
                    break;
                }
            }
        }
        return clausesCopy.size() == 0;
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
        for (Clause clause : clauses) {
            if (clause.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private List<Clause> getClausesDeepCopy() {
        List<Clause> clausesDeepCopy = new ArrayList<>();
        for (Clause clause : clauses) {
            clausesDeepCopy.add(clause.getDeepCopy());
        }
        return clausesDeepCopy;
    }
}
