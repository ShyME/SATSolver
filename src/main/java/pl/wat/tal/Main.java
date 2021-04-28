package pl.wat.tal;

import pl.wat.tal.formula.Clause;
import pl.wat.tal.formula.ConjunctiveNormalFormula;
import pl.wat.tal.input.reader.CNFReader;
import pl.wat.tal.input.reader.DimacsStdInReader;
import pl.wat.tal.satsolver.DPLLSatSolver;
import pl.wat.tal.satsolver.SatSolver;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CNFReader cnfReader = new DimacsStdInReader();
        ConjunctiveNormalFormula cnf = cnfReader.parseCNF();
        System.out.println(cnf.toString());
        SatSolver satSolver = new DPLLSatSolver();
        boolean sat = satSolver.solve(cnf);
        List<String> result = satSolver.getTrueLiterals();
        System.out.println(sat);
        System.out.println(result);
        System.out.println(checkSAT(cnf, result));

    }

    //TODO CHANGE IMPL
    private static boolean checkSAT(ConjunctiveNormalFormula cnf, List<String> trueLiterals) {
        int satisfiedClauses = 0;
        for(Clause clause : cnf.getClauses()) {
            for(String literal : trueLiterals) {
                if(clause.contains(literal)) {
                    satisfiedClauses++;
                    break;
                }
            }
        }
        return cnf.getClauseNumber() == satisfiedClauses;
    }
}
