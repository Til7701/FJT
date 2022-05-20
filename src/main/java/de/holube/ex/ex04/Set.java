package de.holube.ex.ex04;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Semaphore;

public class Set<E> implements Iterable<E> {

    private final Semaphore mutex = new Semaphore(1);

    private final List<E> data;
    private int activeIterators = 0;

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
        try {
            mutex.acquire();
            waitForIterators();
            if (contains(elem)) return false;
            return data.add(elem);
        } finally {
            mutex.release();
        }
    }

    /**
     * Returns true if the given element is contained in this set.
     *
     * @param elem the element to check
     * @return true if the element is contained in this set
     */
    public boolean contains(E elem) {
        try {
            mutex.acquire();
            waitForIterators();
            return data.contains(elem);
        } catch (InterruptedException e) {
            // ignore
        } finally {
            mutex.release();
        }
        return false;
    }

    /**
     * Removes the given element from this set. The calling thread is blocked until running iterators are finished.
     *
     * @param elem the element to remove
     * @return true if the element was removed, false if it was not present
     * @throws InterruptedException if the thread was interrupted while waiting
     */
    public boolean remove(E elem) throws InterruptedException {
        try {
            mutex.acquire();
            waitForIterators();
            return data.remove(elem);
        } finally {
            mutex.release();
        }
    }

    private synchronized void waitForIterators() throws InterruptedException {
        while (activeIterators > 0) wait();
    }

    /**
     * Returns the number of elements in this set.
     *
     * @return the number of elements in this set
     */
    public synchronized int size() {
        return data.size();
    }

    /**
     * Returns the capacity of this set.
     *
     * @return the capacity of this set
     */
    public synchronized int capacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public synchronized SetIterator iterator() {
        activeIterators++;
        return new SetIterator(this);
    }

    public class SetIterator implements Iterator<E> {

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
                    endIteration();
                }
                return elem;
            }
        }

        /**
         * Ends the iteration. This method must be called when the iteration is not completed. Otherwise, the
         * iteration will block other threads from modifying the set.
         */
        public void endIteration() {
            synchronized (set) {
                activeIterators--;
                index = data.size();
                set.notifyAll();
            }
        }
    }

}
