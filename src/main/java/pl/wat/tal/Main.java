package pl.wat.tal;

import pl.wat.tal.formula.ConjunctiveNormalFormula;
import pl.wat.tal.input.reader.CNFReader;
import pl.wat.tal.input.reader.DimacsStdInReader;
import pl.wat.tal.satsolver.OptimalDPLLSatSolver;
import pl.wat.tal.satsolver.SATSolver;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        CNFReader cnfReader = new DimacsStdInReader();
        ConjunctiveNormalFormula cnf = cnfReader.parseCNF();
        System.out.println(cnf.toString());
        SATSolver satSolver = new OptimalDPLLSatSolver();
        Map<Integer, Boolean> result = new HashMap<>();
        boolean sat = satSolver.solve(cnf, result);
        System.out.println(sat);
        System.out.println(result);
    }
}
