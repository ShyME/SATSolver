package pl.wat.tal.DPLL.satsolver;

import pl.wat.tal.DPLL.formula.Clause;
import pl.wat.tal.DPLL.formula.ConjunctiveNormalFormula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DPLLSatSolver implements SatSolver {
    private List<String> trueLiterals;

    @Override
    public boolean solve(ConjunctiveNormalFormula cnf) {
        trueLiterals = new LinkedList<>();
        deletedUnitClauses = new ArrayList<>();
        List<Clause> clauses = cnf.getClauses();
        List<NewClause> newClauses = new ArrayList<>();
        for (int i = 0; i < clauses.size(); i++) {
            newClauses.add(new NewClause(copyList(clauses.get(i).getLiterals())));
        }
        newCNF = new NewConjunctiveNormalFormula(newClauses);
        return literalSolve(true) || literalSolve(false);
    }

    @Override
    public List<String> getTrueLiterals() {
        return trueLiterals.stream().distinct().collect(Collectors.toList());
    }

    private boolean literalSolve(boolean right) {
        //System.out.println(newCNF.getActiveClauses());
        List<String> deletedUnitLiterals = processUnitClauses();

        if(newCNF.containsAnyEmptyClause()) {
            System.out.println("Contains empty clause!");
            newCNF.backtrackUnitClauses(deletedUnitLiterals, deletedUnitClauses);
            return false;
        } else if(cnfCopy.isEmpty()) {
            System.out.println("No clauses left!");
            return true;
        } else {
            String chosenLiteral = cnfCopy.getClauses().get(0).getLiterals().get(0);
            if(!right) {
                chosenLiteral = negateLiteral(cnfCopy.getClauses().get(0).getLiterals().get(0));
            }
            System.out.println("Chosen literal: " + chosenLiteral + ", deleting!");
            removeVariable(chosenLiteral);
            //System.out.println(newCNF.getActiveClauses());
            if(!literalSolve(true) && !literalSolve(false)) {
                newCNF.backtrackChosenLiteral(chosenLiteral);
                newCNF.backtrackUnitClauses(deletedUnitLiterals, deletedUnitClauses);
                return false;
            } else {
                trueLiterals.add(chosenLiteral);
                trueLiterals.addAll(deletedUnitLiterals);
                return true;
            }
        }
    }

    private List<String> copyList(List<String> listToCopy) {
        return new LinkedList<>(listToCopy);
    }

    private List<String> processUnitClauses() {
        // TODO dac modyfikacje deletedunitclauses
        List<String> unitClauseLiterals = new LinkedList<>();
        System.out.println("Processing unit clauses: ");
        List<NewClause> clauses = newCNF.getActiveClauses();
        for(int i = 0; i < clauses.size(); i++) {
            Clause clause = clauses.get(i);
            if (clause.isUnitClause()) {
                System.out.println("Unit literal: ");
                String unitLiteral = clause.getActiveLiterals().get(0);
                System.out.println("Deleting: " + unitLiteral);
                //System.out.println(clauses);
                unitClauseLiterals.add(unitLiteral);

                removeVariable(cnf, unitLiteral);
                i = 0;
            }
        }
        return unitClauseLiterals;
    }

    private void removeVariable(ConjunctiveNormalFormula cnf, String literal) {
        cnf.removeContainingClauses(literal);
        String negatedLiteral = negateLiteral(literal);
        cnf.removeLiteralFromClauses(negatedLiteral);
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
}
