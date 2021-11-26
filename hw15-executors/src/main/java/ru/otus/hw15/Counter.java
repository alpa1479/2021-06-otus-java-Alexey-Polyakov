package ru.otus.hw15;

public class Counter {

    private int value;
    private boolean desc;

    public Counter(int value) {
        this.value = value;
    }

    public int nextValue() {
        checkOrder();
        return desc ? --value : ++value;
    }

    private void checkOrder() {
        if (value == 10) {
            desc = true;
        } else if (value == 1) {
            desc = false;
        }
    }
}
