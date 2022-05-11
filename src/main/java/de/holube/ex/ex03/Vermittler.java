package de.holube.ex.ex03;

/**
 * A class to represent a vermittler. This provides the functionality to swap two values of a given type between two
 * threads.
 *
 * @param <V> the type to swap
 */
public class Vermittler<V> {

    private Data currentData;

    /**
     * This method returns the value of the other thread.
     * The first call to this method will block until the other thread has set its value.
     *
     * @param v the value to give to the other thread
     * @return the value of the other thread
     * @throws InterruptedException if the thread is interrupted while waiting for the second thread.
     */
    public synchronized V tauschen(V v) throws InterruptedException {
        if (currentData == null) {
            currentData = new Data();
            currentData.firstValue = v;
            Data myData = currentData;
            while (myData.secondValue == null) {
                wait();
            }
            return myData.secondValue;
        } else {
            currentData.secondValue = v;
            notifyAll();
            Data myData = currentData;
            currentData = null;
            return myData.firstValue;
        }
    }

    private class Data {
        private V firstValue;
        private V secondValue;
    }

}
