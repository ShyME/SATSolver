package pl.wat.tal;

import lombok.Getter;

public class MemoryCounter {

    private static MemoryCounter instance;
    @Getter
    private int intCounter = 0;
    @Getter
    private int stringCounter = 0;
    @Getter
    private int booleanCounter = 0;

    private MemoryCounter() {
    }

    public static MemoryCounter getInstance() {
        if(instance == null) {
            instance = new MemoryCounter();
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

    public void clear() {
        intCounter = 0;
        stringCounter = 0;
        booleanCounter = 0;
    }
}
