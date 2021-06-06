package pl.wat.tal.memoryCounter;

import lombok.Getter;

public class ComplexityCounter {
    // TODO DPLL operations and memory
    // TODO GSAT operations and memory
    // TODO WalkSAT memory
    // TODO memory show in main function

    private static ComplexityCounter instance;
    @Getter
    private int intCounter = 0;
    @Getter
    private int stringCounter = 0;
    @Getter
    private int booleanCounter = 0;
    @Getter
    private int operationCounter = 0;

    private ComplexityCounter() {
    }

    public static ComplexityCounter getInstance() {
        if(instance == null) {
            instance = new ComplexityCounter();
        }
        return instance;
    }

    public void incrementIntCounter(int howMuch) {
        intCounter += howMuch;
    }

    public void incrementStringCounter(String text) {
        stringCounter += text.length();
    }

    public void incrementBooleanCounter(int howMuch) {
        booleanCounter += howMuch;
    }

    public void incrementOperationCounter(int howMuch) {
        operationCounter += howMuch;
    }

    public void clear() {
        intCounter = 0;
        stringCounter = 0;
        booleanCounter = 0;
        operationCounter = 0;
    }
}
