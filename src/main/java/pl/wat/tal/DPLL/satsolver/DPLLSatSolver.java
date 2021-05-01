package pl.wat.tal.DPLL.satsolver;

import pl.wat.tal.DPLL.formula.Clause;
import pl.wat.tal.DPLL.formula.ConjunctiveNormalFormula;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DPLLSatSolver implements SatSolver {
    private List<String> trueLiterals;

    @Override
    public boolean solve(ConjunctiveNormalFormula cnf) {
        trueLiterals = new ArrayList<>();
        return literalSolve(cnf, true) || literalSolve(cnf, false);
    }

    @Override
    public List<String> getTrueLiterals() {
        return trueLiterals.stream().distinct().collect(Collectors.toList());
    }

    private boolean literalSolve(ConjunctiveNormalFormula cnf, boolean right) {
        ConjunctiveNormalFormula cnfCopy = cnf.copy();
//        System.out.println(cnfCopy.getClauses());
        List<String> deletedUnitLiterals = processUnitClauses(cnfCopy);

        if(cnfCopy.containsAnyEmptyClause()) {
//            System.out.println("Contains empty clause!");
            return false;
        } else if(cnfCopy.isEmpty()) {
            System.out.println("No clauses left!");
            return true;
        } else {
            String chosenLiteral = cnfCopy.getClauses().get(0).getLiterals().get(0);
            if(!right) {
                chosenLiteral = negateLiteral(cnfCopy.getClauses().get(0).getLiterals().get(0));
            }
//            System.out.println("Chosen literal: " + chosenLiteral + ", deleting!");
            removeVariable(cnfCopy, chosenLiteral);
//            System.out.println(cnfCopy.getClauses());
            if(!literalSolve(cnfCopy, true) && !literalSolve(cnfCopy, false)) {
                return false;
            } else {
                trueLiterals.add(chosenLiteral);
                trueLiterals.addAll(deletedUnitLiterals);
                return true;
            }
        }
    }

    private List<String> processUnitClauses(ConjunctiveNormalFormula cnf) {
        List<String> unitClauseLiterals = new ArrayList<>();
//        System.out.println("Processing unit clauses: ");
        List<Clause> clauses = cnf.getClauses();
        for(int i = 0; i < clauses.size(); i++) {
            Clause clause = clauses.get(i);
            if (clause.isUnitClause()) {
//                System.out.println("Unit literal: ");
                String unitLiteral = clause.getLiterals().get(0);
//                System.out.println("Deleting: " + unitLiteral);
//                System.out.println(clauses);
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
