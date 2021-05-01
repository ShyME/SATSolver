package pl.wat.tal.DPLL;

import pl.wat.tal.DPLL.formula.Clause;
import pl.wat.tal.DPLL.formula.ConjunctiveNormalFormula;
import pl.wat.tal.DPLL.reader.CNFReader;
import pl.wat.tal.DPLL.reader.DimacsStdInReader;
import pl.wat.tal.DPLL.satsolver.DPLLSatSolver;
import pl.wat.tal.DPLL.satsolver.SatSolver;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        CNFReader cnfReader = new DimacsStdInReader();
        ConjunctiveNormalFormula cnf = cnfReader.parseCNF();
        System.out.println(cnf.toString());
        SatSolver satSolver = new DPLLSatSolver();
        boolean sat = satSolver.solve(cnf);
//        List<String> result = satSolver.getTrueLiterals();
//        System.out.println(sat);
//        System.out.println(result);
//        System.out.println(checkSAT(cnf, result));


        List<Long> times = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            long dt =  System.currentTimeMillis();
            System.out.println(satSolver.solve(cnf));
            long et = System.currentTimeMillis();
            System.out.println(et - dt);

            times.add(et - dt);
        }

        double avg = 0.0;
        for (int i = 0; i < times.size(); i++) {
            avg += times.get(i);
        }

        System.out.println("Średnia");
        System.out.println(avg / 20);

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
