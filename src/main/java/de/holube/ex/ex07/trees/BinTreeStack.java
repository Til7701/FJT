package de.holube.ex.ex07.trees;

public class BinTreeStack {
    private BinTree[] values;
    private int current;

    public BinTreeStack() {
        values = new BinTree[10];
        current = -1;
    }

    public boolean isEmpty() {
        return current == -1;
    }

    public void push(BinTree value) {
        ensureCapacity();
        values[++current] = value;
    }

    public BinTree pop() {
        if (isEmpty()) {
            return null; // error
        }
        return values[this.current--];
    }

    public BinTree peek() {
        if (isEmpty()) {
            return null; // error
        }
        return values[current];
    }

    void ensureCapacity() {
        if (current == values.length - 1) {
            BinTree[] help = new BinTree[values.length * 2];
            for (int i = 0; i < values.length; i++) {
                help[i] = values[i];
            }
            values = help;
        }
    }

}
