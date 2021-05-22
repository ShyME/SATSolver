package pl.wat.tal.GSAT.input;

import pl.wat.tal.DPLL.Main;
import pl.wat.tal.DPLL.formula.Clause;
import pl.wat.tal.DPLL.reader.CNFReader;
import pl.wat.tal.DPLL.reader.DimacsStdInReader;
import pl.wat.tal.DPLL.satsolver.DPLLSatSolver;
import pl.wat.tal.DPLL.satsolver.SatSolver;
import pl.wat.tal.GSAT.formula.ConjunctiveNormalFormula;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataGeneratorImpl implements DataGenerator {

    public List<Clause> generate(int numberOfVariables, int numberOfClauses) throws InterruptedException {
        List<Clause> clauses = new ArrayList<>();
        boolean formulaIsSolvable = false;
        while (!formulaIsSolvable) {
            clauses = new ArrayList<>();
            for (int i = 0; i < numberOfClauses; i++) {
                clauses.add(new Clause(generateLiterals(numberOfVariables)));
            }

            long start = System.currentTimeMillis();
            formulaIsSolvable = checkWithDpll(clauses, numberOfVariables, numberOfClauses);
            if (System.currentTimeMillis() - start < 1000) formulaIsSolvable = false;
        }
        return clauses;
    }

    private boolean checkWithDpll(List<Clause> clauses, int numberOfVariables, int numberOfClauses) {
        CNFReader cnfReader = new DimacsStdInReader();


        pl.wat.tal.DPLL.formula.ConjunctiveNormalFormula cnf = new pl.wat.tal.DPLL.formula.ConjunctiveNormalFormula(numberOfVariables, numberOfClauses, clauses);
//        ConjunctiveNormalFormula cnf = cnfReader.parseCNF();

        System.out.println("generacja");
//        System.out.println(cnf.toString());
        SatSolver satSolver = new DPLLSatSolver();
        boolean sat = satSolver.solve(cnf);
        System.out.println(sat);
        return sat;
//        List<String> result = satSolver.getTrueLiterals();
//        System.out.println(sat);
//        System.out.println(result);
//        System.out.println(checkSAT(cnf, result));
    }

    private static List<String> generateLiterals(int numberOfVariables) {
        Random random = new Random();

//        int numberOfLiterals = random.nextInt(5) + 1;
        int numberOfLiterals = 3;
//        int numberOfLiterals = random.nextInt(numberOfVariables) + 1;
        List<String> literals = new ArrayList<>();
        for (int i = 0; i < numberOfVariables; i++) {

            literals.add((random.nextInt(10)>5? "" : "-") + i);
        }

//        List<String> literals = IntStream.rangeClosed(1, numberOfVariables).mapToObj(
//                literal -> ((random.nextBoolean()? "" : "-") + literal)
//        ).collect(Collectors.toList());
        //this was slightly slower

        Collections.shuffle(literals);

        return literals.subList(0, numberOfLiterals);
    }

    public ConjunctiveNormalFormula generateCNFGsat(List<Clause> clauses){

        List<String> distinctLiterals = clauses.stream()
                .flatMap(clause -> clause.getLiterals().stream())
                .map(literal -> literal.replace("-", ""))
                .distinct()
                .collect(Collectors.toList());

        return new ConjunctiveNormalFormula(clauses, distinctLiterals);
    }
}
