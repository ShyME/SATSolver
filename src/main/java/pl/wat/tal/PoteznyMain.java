package pl.wat.tal;

import pl.wat.tal.DPLL.Main;
import pl.wat.tal.DPLL.formula.Clause;
import pl.wat.tal.GSAT.input.DataGeneratorImpl;

import java.util.List;
import java.util.stream.Collectors;

public class PoteznyMain {

    public static void main(String[] args) {

        int variables = 3000;
        int clauseNumber = 8100;


        long dt =  System.currentTimeMillis();
        List<Clause> clauses = new DataGeneratorImpl().generate(variables, clauseNumber);

        long et =  System.currentTimeMillis();
        System.out.println("generating " + (et - dt));

        Main.main(clauses, variables, clauseNumber);


        pl.wat.tal.GSAT.Main.avgExecTime(clauses);
    }
}
