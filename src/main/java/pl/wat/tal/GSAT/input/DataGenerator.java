package pl.wat.tal.GSAT.input;
import pl.wat.tal.GSAT.formula.Clause;

import java.util.List;

public interface DataGenerator {
    List<Clause> generate(int numberOfVariables, int numberOfClauses);
}
