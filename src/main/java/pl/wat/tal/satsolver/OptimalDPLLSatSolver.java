package pl.wat.tal.satsolver;

import pl.wat.tal.formula.Clause;
import pl.wat.tal.formula.ConjunctiveNormalFormula;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptimalDPLLSatSolver implements SATSolver {

    @Override
    public boolean solve(ConjunctiveNormalFormula cnf, Map<Integer, Boolean> resultMap) {
        ConjunctiveNormalFormula cnfCopy = cnf.copy();
        System.out.println(cnfCopy.getClauses());
        Map<Integer, Boolean> resultMapCopy = new HashMap<>(resultMap);

        processUnitClauses(cnfCopy, resultMapCopy);
        if(cnfCopy.containsAnyEmptyClause()) {
            return false;
        } else if(cnfCopy.isEmpty()) {
            return true;
        } else {
            String literal = cnfCopy.getClauses().get(0).getLiterals().get(0);
            System.out.println("First way on " + literal);

            ConjunctiveNormalFormula cnfCopy2 = cnfCopy.copy();
            removeVariable(cnfCopy, literal);
            System.out.println(cnfCopy.getClauses());
            if(!solve(cnfCopy, resultMapCopy)) {
                String literalToNegate = cnfCopy2.getClauses().get(0).getLiterals().get(0);
                String negatedLiteral = negateLiteral(literalToNegate);
                System.out.println("Other way on " + literalToNegate);
                removeVariable(cnfCopy2, negatedLiteral);
                System.out.println(cnfCopy2.getClauses());
                return solve(cnf, resultMap);
            } else {
                return true;
            }
        }
    }

    @Override
    public boolean solve2(ConjunctiveNormalFormula cnf, Map<Integer, Boolean> resultMap) {
        ConjunctiveNormalFormula cnfCopy = cnf.copy();
        Map<Integer, Boolean> resultMapCopy = new HashMap<>(resultMap);

        processUnitClauses(cnfCopy, resultMapCopy);
        if(cnfCopy.containsAnyEmptyClause()) {
            return false;
        } else if(cnfCopy.isEmpty()) {
            return true;
        } else {
            String literal = cnfCopy.getClauses().get(0).getLiterals().get(0);

            removeVariable(cnfCopy, literal);
            return solve(cnfCopy, resultMapCopy);
        }
    }

    private void processUnitClauses(ConjunctiveNormalFormula cnf, Map<Integer, Boolean> resultMap) {
        List<Clause> clauses = cnf.getClauses();
        for(int i = 0; i < clauses.size(); i++) {
            Clause clause = clauses.get(i);
            if (clause.isUnitClause()) {
                String unitLiteral = clause.getLiterals().get(0);
                System.out.println("Deleting: " + unitLiteral);
                System.out.println(clauses);

                int literalVariableNumber = getLiteralVariableNumber(unitLiteral);
                boolean unavoidableValue = !isLiteralNegated(unitLiteral);
                //resultMap.put(literalVariableNumber, unavoidableValue);

                removeVariable(cnf, unitLiteral);
                i = 0;
            }
        }
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

    private int getLiteralVariableNumber(String literal) {
        return Integer.parseInt(literal.replace("-", ""));
    }
}
