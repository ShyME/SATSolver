package pl.wat.tal.DPLL.reader;

import pl.wat.tal.DPLL.formula.Clause;
import pl.wat.tal.DPLL.formula.ConjunctiveNormalFormula;
import pl.wat.tal.GSAT.input.DataGeneratorImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DimacsStdInReader implements CNFReader {
    private List<String> pLines;
    private List<String> clauseLines;

    @Override
    public ConjunctiveNormalFormula parseCNF() {
        pLines = new ArrayList<>();
        clauseLines = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;

            while((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
                parseDimacsLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buildCNF();
    }

    private void parseDimacsLine(String line) {
        if(!isLineComment(line)) {
            if(isCNFProblemDefinition(line)) {
                pLines.add(line);
            } else {
                clauseLines.add(line);
            }
        }
    }

    private ConjunctiveNormalFormula buildCNF() {
        int[] vars = parseProblemDefinition();
        List<Clause> clauses = parseClauses();

        int variableNumber = vars[0];
        int clauseNumber = vars[1];
        return new ConjunctiveNormalFormula(variableNumber, clauseNumber, clauses);
    }

    private int[] parseProblemDefinition() {
        if(pLines.size() != 1)
            throw new IllegalArgumentException("Dimacs input has to contain one problem defining line.");
        String[] tokens = pLines.get(0).trim().split(" +");
        if(tokens.length != 4)
            throw new IllegalArgumentException("Dimacs problem statement has to define number of variables and clauses.");
        int variableNumber = Integer.parseInt(tokens[2]);
        int clauseNumber = Integer.parseInt(tokens[3]);
        return new int[]{variableNumber, clauseNumber};
    }

    private List<Clause> parseClauses() {
        List<Clause> clauses = new ArrayList<>();
        String[] tokens;
        List<String> clauseLiterals = new ArrayList<>();
        for (String line : clauseLines) {
            tokens = line.trim().split(" +");
            if(!line.endsWith("0")) {
                clauseLiterals.addAll(Arrays.asList(tokens));
            } else {
                tokens = Arrays.copyOf(tokens, tokens.length - 1);
                clauseLiterals.addAll(Arrays.asList(tokens));
                clauses.add(new Clause(clauseLiterals));
                clauseLiterals = new ArrayList<>();
            }
        }
        return clauses;
    }

    private boolean isLineComment(String line) {
        return line.charAt(0) == 'c';
    }

    private boolean isCNFProblemDefinition(String line) {
        if(line.length() >= 5) {
            return line.substring(0, 5).trim().equalsIgnoreCase("p cnf");
        }
        return false;
    }
}
