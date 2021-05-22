package pl.wat.tal.DPLL.formula;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class NewConjunctiveNormalFormula {
    private List<NewClause> activeClauses;
    private List<NewClause> removedClauses;

    public NewConjunctiveNormalFormula(List<NewClause> clauses) {
        this.activeClauses = clauses;
        removedClauses = new ArrayList<>();
    }

    public void backtrackChosenLiteral(String literal) {
        System.out.println("Backtracking on literal:" + literal);
        System.out.println(this);
        // backtrack na jednym literale, ktory zakladalismy ze byl prawdziwy ->
        // jego true value oznacza ze usuwamy klauzule - wiec w usunietych klauzulach szukamy jego aktywnej wartosci i uaktywniamy
        // jego negated value bylo usuniete w innych klauzulach, wiec dodajemy

        List<NewClause> removedClausesToActivate = removedClauses.stream().filter(clause -> clause.contains(literal)).collect(Collectors.toList());

        activeClauses.addAll(removedClausesToActivate);
        removedClauses.removeAll(removedClausesToActivate);


        String negatedLiteral = negateLiteral(literal);
        List<NewClause> removedClausesToModifyWithNegatedLiteral = removedClauses.stream().filter(clause -> clause.contained(negatedLiteral)).collect(Collectors.toList());
        List<NewClause> activeClausesToModifyWithNegatedLiteral = activeClauses.stream().filter(clause -> clause.contained(negatedLiteral)).collect(Collectors.toList());

        activeClausesToModifyWithNegatedLiteral.forEach(clause -> clause.backtrack(negatedLiteral));
        removedClausesToModifyWithNegatedLiteral.forEach(clause -> clause.backtrack(negatedLiteral));
        System.out.println(this);
    }

    public void backtrackUnitClauses(List<String> literals, List<NewClause> deletedUnitClauses) {
        System.out.println("Before unit clauses backtrack");
        System.out.println(this);
        // backtrack na unit clausach - te klauzule mialy jeden literal
        // wiec trzeba im przywrocic te literaly, oraz je uaktywnic
        // zanegowany literal po prostu przywrocic w usunietych i aktywnych

        for(String literal : literals) {
            // ta czesc zamieniac na pwrzywracanie z listy unit claus
            /*List<NewClause> removedClausesWithUnitLiteral = removedClauses.stream().filter(clause -> clause.contains(literal)).collect(Collectors.toList());
            removedClausesWithUnitLiteral.forEach(clause -> clause.backtrack(literal));
            activeClauses.addAll(removedClausesWithUnitLiteral);
            removedClauses.removeAll(removedClausesWithUnitLiteral);*/
            //todo tutaj przywrocic unit klauzule z deletedunitclauses




            String negatedLiteral = negateLiteral(literal);
            List<NewClause> removedClausesToModifyWithNegatedLiteral = removedClauses.stream().filter(clause -> clause.contained(negatedLiteral)).collect(Collectors.toList());
            List<NewClause> activeClausesToModifyWithNegatedLiteral = activeClauses.stream().filter(clause -> clause.contained(negatedLiteral)).collect(Collectors.toList());
            activeClausesToModifyWithNegatedLiteral.forEach(clause -> clause.backtrack(negatedLiteral));
            removedClausesToModifyWithNegatedLiteral.forEach(clause -> clause.backtrack(negatedLiteral));
        }
        System.out.println(this);

    }

    private String negateLiteral(String literal) {
        if (isLiteralNegated(literal)) {
            return literal.substring(1);
        } else {
            return "-" + literal;
        }
    }

    private boolean isLiteralNegated(String literal) {
        return literal.contains("-");
    }

    public void removeContainingClauses(String literal) {
        removedClauses.addAll(activeClauses.stream().filter(clause -> clause.contains(literal)).collect(Collectors.toList()));
        activeClauses.removeIf(clause -> clause.contains(literal));
    }

    public void removeLiteralFromClauses(String literal) {

        activeClauses.forEach(c -> {
            if(c.contains(literal)) {
                c.remove(literal);
            }
        });
    }

    public boolean isEmpty() {
        return activeClauses.isEmpty();
    }

    public boolean containsAnyEmptyClause() {
        for(NewClause clause : activeClauses) {
            if(clause.isEmpty()) {
                return true;
            }
        }
        return false;
    }

}
