package pl.wat.tal.GSAT;


import pl.wat.tal.DPLL.formula.Clause;
import pl.wat.tal.GSAT.alg.Gsat;
import pl.wat.tal.GSAT.formula.ConjunctiveNormalFormula;
import pl.wat.tal.GSAT.formula.VariableData;
import pl.wat.tal.GSAT.input.DataGeneratorImpl;
import pl.wat.tal.GSAT.input.DimacsStdInReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

//        singleExecStats("cnf2.txt");
//        avgExecTime("cnf2.txt");

    }

    public static void singleExecStats(List<Clause> clauses){
//        DimacsStdInReader inReader = new DimacsStdInReader();
//        ConjunctiveNormalFormula formula = inReader.readFormulaFromFile(fileName);

        long start = System.currentTimeMillis();

        ConjunctiveNormalFormula formula = new DataGeneratorImpl().generateCNFGsat(clauses);
        System.out.println("dupa" + (System.currentTimeMillis() - start));

        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        Gsat gsat = new Gsat();
        long dt =  System.currentTimeMillis();
        Map<String, VariableData> result = gsat.findSolution(formula, 200, 300, 0.9);

        System.out.println("variables: " + result.size());

        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        System.out.println(result);
        System.out.println("Czas wykonania [ms]: " + (System.currentTimeMillis() - dt));
        System.out.println("Ilość zajętej pamięci: " + (afterUsedMem-beforeUsedMem));
        System.out.println("Czy formuła jest spełniona dla powyższego wartościowania: " + formula.isSatisfied(result));
    }

    public static void avgExecTime(List<Clause> clauses){
        DimacsStdInReader inReader = new DimacsStdInReader();
        ConjunctiveNormalFormula formula = new DataGeneratorImpl().generateCNFGsat(clauses);
        Gsat gsat = new Gsat();

        List<Long> times = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            long dt =  System.currentTimeMillis();
            gsat.findSolution(formula, 200, 1500, 0.8);
            long et = System.currentTimeMillis();
            System.out.println("gsat done " + (et - dt));

            times.add(et - dt);
        }

        double avg = 0.0;
        for (int i = 0; i < times.size(); i++) {
            avg += times.get(i);
        }

        System.out.println("Średnia");
        System.out.println(avg / 20);
    }
}
