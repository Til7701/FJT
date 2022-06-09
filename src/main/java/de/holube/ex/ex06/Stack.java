package de.holube.ex.ex06;

import java.util.concurrent.atomic.AtomicReference;

public class Stack<T> {

    private final AtomicReference<Item<T>> top = new AtomicReference<>();

    public void push(T element) {
        Item<T> currentTop = top.get();
        Item<T> newTop = new Item<>(element);
        newTop.next = currentTop;

        while (!top.compareAndSet(currentTop, newTop)) {
            currentTop = top.get();
            newTop.next = currentTop;
        }
    }

    public T pop() {
        Item<T> currentTop = top.get();
        if (currentTop == null)
            return null;

        Item<T> newTop = currentTop.next;
        while (!top.compareAndSet(currentTop, newTop)) {
            currentTop = top.get();
            newTop = currentTop.next;
        }

        return currentTop.value;
    }

    public T peek() {
        Item<T> currentTop = top.get();
        if (currentTop == null) return null;
        return currentTop.value;
    }

    public boolean isEmpty() {
        return top.compareAndSet(null, null);
    }

    private static class Item<T> {

        final T value;
        Item<T> next;

        public Item(T value) {
            this.value = value;
        }

    }

}
