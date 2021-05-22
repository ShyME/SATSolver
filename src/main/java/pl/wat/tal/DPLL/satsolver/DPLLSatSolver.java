package pl.wat.tal.DPLL.satsolver;

import pl.wat.tal.DPLL.formula.Clause;
import pl.wat.tal.DPLL.formula.ConjunctiveNormalFormula;
import pl.wat.tal.DPLL.reader.CNFReader;
import pl.wat.tal.DPLL.reader.DimacsStdInReader;
import pl.wat.tal.SatSolver;

import java.util.ArrayList;
import java.util.List;

public class DPLLSatSolver implements SatSolver {
    private List<String> trueLiterals;

    private final int VARIABLE_NUMBER;
    private final int CLAUSE_NUMBER;

    public DPLLSatSolver(int VARIABLE_NUMBER, int CLAUSE_NUMBER) {
        this.VARIABLE_NUMBER = VARIABLE_NUMBER;
        this.CLAUSE_NUMBER = CLAUSE_NUMBER;
    }

    @Override
    public boolean solve(List<Clause> clauses) {
        return solveCNF(new ConjunctiveNormalFormula(VARIABLE_NUMBER, CLAUSE_NUMBER, clauses));
    }

    @Override
    public boolean solveFromFile(String filename) {
        CNFReader cnfReader = new DimacsStdInReader();
        ConjunctiveNormalFormula cnf = cnfReader.parseCNF();
        return solveCNF(cnf);
    }

    @Override
    public List<String> getResult() {
//        return trueLiterals.stream().distinct().collect(Collectors
//                .toMap(x -> x.replace("-", "")
//                        , x -> new VariableData(!x.contains("-"))));
        return trueLiterals;
    }

    private boolean solveCNF(ConjunctiveNormalFormula cnf) {
        trueLiterals = new ArrayList<>();
        return literalSolve(cnf, true) || literalSolve(cnf, false);
    }

    private boolean literalSolve(ConjunctiveNormalFormula cnf, boolean right) {
        ConjunctiveNormalFormula cnfCopy = cnf.copy();
        List<String> deletedUnitLiterals = processUnitClauses(cnfCopy);

        if (cnfCopy.containsAnyEmptyClause()) {
            return false;
        } else if (cnfCopy.isEmpty()) {
            trueLiterals.addAll(deletedUnitLiterals);
            return true;
        } else {
            String chosenLiteral = cnfCopy.getClauses().get(0).getLiterals().get(0);
            if (!right) {
                chosenLiteral = negateLiteral(cnfCopy.getClauses().get(0).getLiterals().get(0));
            }
            removeVariable(cnfCopy, chosenLiteral);
            if (!literalSolve(cnfCopy, true) && !literalSolve(cnfCopy, false)) {
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
        List<Clause> clauses = cnf.getClauses();
        for (int i = 0; i < clauses.size(); i++) {
            Clause clause = clauses.get(i);
            if (clause.isUnitClause()) {
                String unitLiteral = clause.getLiterals().get(0);
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
