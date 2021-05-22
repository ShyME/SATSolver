package pl.wat.tal;

import pl.wat.tal.DPLL.formula.Clause;
import pl.wat.tal.DPLL.formula.ConjunctiveNormalFormula;
import pl.wat.tal.DPLL.satsolver.DPLLSatSolver;
import pl.wat.tal.generator.DataGenerator;
import pl.wat.tal.heuristic.alg.GSat;
import pl.wat.tal.heuristic.alg.WalkSat;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        final int VARIABLE_NUMBER = 100;
        final int CLAUSE_NUMBER = 403;
        final int N = 5;

        System.out.println("Generating SAT problem..");
        long start = System.currentTimeMillis();
        List<Clause> clauses = new DataGenerator().generate(VARIABLE_NUMBER, CLAUSE_NUMBER);
        long end = System.currentTimeMillis();
        System.out.println("Time spent on generating: " + (end - start) + "ms.\n");

        SatSolver dpllSatSolver = new DPLLSatSolver(VARIABLE_NUMBER, CLAUSE_NUMBER);
        double avgDpll = runSolverInLoop(dpllSatSolver, clauses, N, "DPLL");

        SatSolver walkSatSolver = new WalkSat(200, 200, 0.0);
        double avgWSAT = runSolverInLoop(walkSatSolver, clauses, N, "WalkSAT");

        SatSolver gsatSolver = new GSat(200, 2000, 0.0);
        double avgGSAT = runSolverInLoop(gsatSolver, clauses, N, "GSAT");

        System.out.println();
        System.out.println("Average DPLL time for " + N + " tries: " + avgDpll + "ms.");
        System.out.println("Average WalkSAT time for " + N + " tries: " + avgWSAT + "ms.");
        System.out.println("Average GSAT time for " + N + " tries: " + avgGSAT + "ms.");
        System.out.println();
        System.out.println("DPLL last results:");
        System.out.println(dpllSatSolver.getResult());
        System.out.println("WalkSAT last results:");
        System.out.println(walkSatSolver.getResult());
        System.out.println("GSAT last results:");
        System.out.println(gsatSolver.getResult());
        System.out.println();


        System.out.println("DPLL last check: " + ConjunctiveNormalFormula.checkSAT(clauses, dpllSatSolver.getResult()));
        System.out.println("WalkSAT last check: " + ConjunctiveNormalFormula.checkSAT(clauses, walkSatSolver.getResult()));
        System.out.println("GSAT last check: " + ConjunctiveNormalFormula.checkSAT(clauses, gsatSolver.getResult()));
    }

    public static double runSolverInLoop(SatSolver satSolver, List<Clause> clauses, int n, String solver) {
        long sumTime = 0;
        long start, end;
        boolean result;
        for (int i = 0; i < n; i++) {
            start = System.currentTimeMillis();
            result = satSolver.solve(clauses);
            end = System.currentTimeMillis();
            System.out.println(solver + " result: " + result + " in time " + (end - start) + "ms.");
            sumTime += end - start;
        }
        System.out.println();
        return (double) sumTime / n;
    }
}
