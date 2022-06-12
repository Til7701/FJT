package de.holube.ex.ex06;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * A class to represent a vermittler. This provides the functionality to swap two values of a given type between two
 * threads.
 *
 * @param <V> the type to swap
 * @author Tilman Holube
 */
public class Vermittler<V> {

    private final AtomicStampedReference<Pair<V, V>> data = new AtomicStampedReference<>(null, 0);

    /**
     * This method returns the value of the other thread.
     * The first call to this method will block until the other thread has set its value.
     * The second call to this method will not return immediately. There is no fairness guaranteed.
     *
     * @param inValue the value to give to the other thread
     * @return the value of the other thread
     * @throws InterruptedException if the thread is interrupted while waiting for the second thread.
     */
    public V tauschen(V inValue) throws InterruptedException {
        final int[] stampHolder = {-1};
        final Pair<V, V> newPair = new Pair<>();
        while (true) {
            Pair<V, V> oldPair = data.get(stampHolder);
            newPair.setFirst(inValue);
            newPair.setSecond(null);

            if (data.compareAndSet(null, newPair, 0, 1)) { // first thread setting value
                return waitForSecond();
            }

            newPair.setSecond(inValue);
            if (data.compareAndSet(oldPair, newPair, 1, 2)) { // second thread setting value
                return oldPair.getFirst();
            }

            if (Thread.currentThread().isInterrupted()) {
                data.set(null, 0);
                throw new InterruptedException();
            }
        }
    }

    private V waitForSecond() throws InterruptedException {
        final int[] markHolder = {-1};
        Pair<V, V> pair = data.get(markHolder);
        while (!data.compareAndSet(pair, null, 2, 0)) {
            //Thread.yield();
            pair = data.get(markHolder);
            if (Thread.currentThread().isInterrupted()) {
                data.set(null, 0);
                throw new InterruptedException();
            }
        }
        return pair.getSecond();
    }

    private static class Pair<X, Y> {

        private X first = null;
        private Y second = null;

        public X getFirst() {
            return first;
        }

        public void setFirst(X first) {
            this.first = first;
        }

        public Y getSecond() {
            return second;
        }

        public void setSecond(Y second) {
            this.second = second;
        }
    }

}
