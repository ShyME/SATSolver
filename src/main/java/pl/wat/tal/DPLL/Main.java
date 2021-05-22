package pl.wat.tal.DPLL;

import pl.wat.tal.DPLL.formula.Clause;
import pl.wat.tal.DPLL.formula.ConjunctiveNormalFormula;
import pl.wat.tal.DPLL.reader.CNFReader;
import pl.wat.tal.DPLL.reader.DimacsStdInReader;
import pl.wat.tal.DPLL.satsolver.DPLLSatSolver;
import pl.wat.tal.DPLL.satsolver.SatSolver;
import pl.wat.tal.GSAT.input.DataGeneratorImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        CNFReader cnfReader = new DimacsStdInReader();
        SatSolver satSolver = new DPLLSatSolver();
        ConjunctiveNormalFormula cnf = cnfReader.parseCNF();
        ConjunctiveNormalFormula cnfCopy = cnf.copy();
        System.out.println(satSolver.solve(cnf));
        System.out.println(satSolver.getTrueLiterals());
        System.out.println(cnf);
        System.out.println(checkSAT(cnfCopy, satSolver.getTrueLiterals()));
    }

    public static void main(List<Clause> clauses, int variableNumber, int clauseNumber) throws InterruptedException {
        CNFReader cnfReader = new DimacsStdInReader();


        ConjunctiveNormalFormula cnf = new ConjunctiveNormalFormula(variableNumber, clauseNumber, clauses);
//        ConjunctiveNormalFormula cnf = cnfReader.parseCNF();

        System.out.println("dpll wygenerowal cnf");
//        System.out.println(cnf.toString());
        SatSolver satSolver = new DPLLSatSolver();
//        boolean sat = satSolver.solve(cnf);
//        List<String> result = satSolver.getTrueLiterals();
//        System.out.println(sat);
//        System.out.println(result);
//        System.out.println(checkSAT(cnf, result));


        List<Long> times = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            long dt =  System.currentTimeMillis();
            System.out.println(satSolver.solve(cnf));
//            Thread.sleep(2000);
            System.out.println(satSolver.getTrueLiterals());
            long et = System.currentTimeMillis();
            System.out.println(et - dt);

            times.add(et - dt);
        }
//        6.5-9
        double avg = 0.0;
        for (int i = 0; i < times.size(); i++) {
            avg += times.get(i);
        }

        System.out.println("Średnia");
        System.out.println(avg / 5);

    }

    //TODO CHANGE IMPL
    private static boolean checkSAT(ConjunctiveNormalFormula cnf, List<String> trueLiterals) {
        ConjunctiveNormalFormula cnfCopy = cnf.copy();
        for(Clause clause : cnf.getClauses()) {
            for(String literal : trueLiterals) {
                if(clause.contains(literal)) {
                    //System.out.println(clause);
                    cnfCopy.removeContainingClauses(literal);
                    //System.out.println("Literal: " + literal);
                    //System.out.println(cnfCopy.getClauses().size());
                    break;
                }
            }
        }
        return cnfCopy.getClauses().size() == 0;
    }
}
