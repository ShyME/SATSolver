package pl.wat.tal.GSAT.input;


import pl.wat.tal.GSAT.formula.Clause;
import pl.wat.tal.GSAT.formula.ConjunctiveNormalFormula;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DimacsStdInReader implements DataGenerator {

    public ConjunctiveNormalFormula readFormulaFromFile(String fileName){

        try(Stream<String> line = Files.lines(Path.of(fileName))){

            List<Clause> clauses = line
                    .map(literals -> literals.trim().split(" +"))
                    .map(literalsArray -> new Clause(List.of(literalsArray)))
                    .collect(Collectors.toList());

            List<String> distinctLiterals = clauses.stream()
                    .flatMap(clause -> clause.getLiterals().stream())
                    .map(literal -> literal.replace("-", ""))
                    .distinct()
                    .collect(Collectors.toList());

            return new ConjunctiveNormalFormula(clauses, distinctLiterals);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Clause> generate(int numberOfVariables, int numberOfClauses) {
        return null;
    }
}