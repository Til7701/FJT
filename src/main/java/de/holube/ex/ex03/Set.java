package de.holube.ex.ex03;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Set<E> implements Iterable<E> {

    private final List<E> data;
    private int activeIterators = 0;

    public Set(int capacity) {
        data = new ArrayList<>(capacity);
    }

    public synchronized boolean add(E elem) throws InterruptedException {
        while (activeIterators > 0) {
            wait();
        }
        if (contains(elem)) return false;
        return data.add(elem);
    }

    public synchronized boolean contains(E elem) {
        return data.contains(elem);
    }

    public synchronized boolean remove(E elem) throws InterruptedException {
        while (activeIterators > 0) {
            wait();
        }
        return data.remove(elem);
    }

    public synchronized int size() {
        return data.size();
    }

    public synchronized int capacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public synchronized Iterator<E> iterator() {
        activeIterators++;
        return new SetIterator(this);
    }

    private class SetIterator implements Iterator<E> {

        private final Set<E> set;
        private int index = 0;

        private SetIterator(Set<E> set) {
            this.set = set;
        }

        @Override
        public boolean hasNext() {
            synchronized (set) {
                return index < data.size();
            }
        }

        @Override
        public E next() {
            synchronized (set) {
                if (!hasNext()) throw new NoSuchElementException();
                E elem = data.get(index++);
                if (index == data.size()) {
                    activeIterators--;
                }
                return elem;
            }
        }
    }
}
