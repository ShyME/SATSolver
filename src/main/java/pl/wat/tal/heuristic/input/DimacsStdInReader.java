package pl.wat.tal.heuristic.input;


import pl.wat.tal.DPLL.formula.Clause;
import pl.wat.tal.heuristic.formula.ConjunctiveNormalFormula;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DimacsStdInReader {

    public ConjunctiveNormalFormula readFormulaFromFile(String fileName) {

        try (Stream<String> line = Files.lines(Path.of(fileName))) {

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
}
