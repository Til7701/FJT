package de.holube.ex.ex04;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Set<E> implements Iterable<E> {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    private final List<E> data;

    /**
     * Creates a new set with the initial capacity of 10.
     */
    public Set() {
        data = new ArrayList<>(10);
    }

    /**
     * Creates a new set with the given initial capacity.
     */
    public Set(int capacity) {
        data = new ArrayList<>(capacity);
    }

    /**
     * Adds the given element to this set. The calling thread is blocked until running iterators are finished.
     *
     * @param elem the element to add
     * @return true if the element was added, false if it was already present
     * @throws InterruptedException if the thread was interrupted while waiting
     */
    public boolean add(E elem) throws InterruptedException {
        if (contains(elem)) return false;
        writeLock.lock();
        try {
            if (contains(elem)) return false;
            return data.add(elem);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Returns true if the given element is contained in this set.
     *
     * @param elem the element to check
     * @return true if the element is contained in this set
     */
    public boolean contains(E elem) {
        readLock.lock();
        try {
            return data.contains(elem);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Removes the given element from this set. The calling thread is blocked until running iterators are finished.
     *
     * @param elem the element to remove
     * @return true if the element was removed, false if it was not present
     */
    public boolean remove(E elem) {
        writeLock.lock();
        try {
            return data.remove(elem);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Returns the number of elements in this set.
     *
     * @return the number of elements in this set
     */
    public int size() {
        readLock.lock();
        try {
            return data.size();
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Returns the capacity of this set.
     *
     * @return the capacity of this set
     */
    public int capacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public synchronized SetIterator iterator() {
        readLock.lock();
        return new SetIterator();
    }

    public class SetIterator implements Iterator<E> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < data.size();
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            E elem = data.get(index++);
            if (index == data.size()) {
                endIteration();
            }
            return elem;
        }

        /**
         * Ends the iteration. This method must be called when the iteration is not completed. Otherwise, the
         * iteration will block other threads from modifying the set.
         */
        public void endIteration() {
            index = data.size();
            readLock.unlock();
        }
    }

}
