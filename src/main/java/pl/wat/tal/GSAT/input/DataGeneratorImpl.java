package pl.wat.tal.GSAT.input;

import pl.wat.tal.DPLL.formula.Clause;
import pl.wat.tal.GSAT.formula.ConjunctiveNormalFormula;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataGeneratorImpl implements DataGenerator {

    public List<Clause> generate(int numberOfVariables, int numberOfClauses) {
        List<Clause> clauses = new ArrayList<>();
        for (int i = 0; i < numberOfClauses; i++) {
                clauses.add(new Clause(generateLiterals(numberOfVariables)));
        }
        return clauses;
    }

    private static List<String> generateLiterals(int numberOfVariables) {
        Random random = new Random();
        int numberOfLiterals = random.nextInt(numberOfVariables) + 1;
        List<String> literals = new ArrayList<>();
        for (int i = 0; i < numberOfVariables; i++) {
            literals.add((random.nextBoolean()? "" : "-") + i);
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
