package de.holube.ex.ex07.trees;

public class BinTreeQueue {
    private BinTree[] values;
    private int numberOfElements;

    public BinTreeQueue() {
        values = new BinTree[10];
        numberOfElements = 0;
    }

    public boolean isEmpty() {
        return numberOfElements == 0;
    }

    public void enque(BinTree value) {
        ensureCapacity();
        values[numberOfElements] = value;
        numberOfElements++;
    }

    public BinTree deque() {
        if (isEmpty()) {
            return null; // error
        }
        BinTree oldest = values[0];
        for (int i = 0; i < numberOfElements - 1; i++) {
            values[i] = values[i + 1];
        }
        numberOfElements--;
        return oldest;
    }

    public BinTree peek() {
        if (isEmpty()) {
            return null; // error
        }
        return values[0];
    }

    void ensureCapacity() {
        if (numberOfElements == values.length) {
            BinTree[] help = new BinTree[values.length * 2];
            for (int i = 0; i < values.length; i++) {
                help[i] = values[i];
            }
            values = help;
        }
    }

}
