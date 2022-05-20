package de.holube.ex.ex04;

import java.util.concurrent.Semaphore;

/**
 * This class provides a way to wait for a number of threads to finish.
 *
 * @author Tilman Holube
 */
public class Sammelpunkt {

    private final int amount;
    private final Semaphore mutex;
    private Semaphore currentSemaphore;

    /**
     * Creates a new Sammelpunkt.
     *
     * @param amount the amount of threads that should be waiting for the Sammelpunkt
     */
    public Sammelpunkt(int amount) {
        if (amount < 1) {
            amount = 1;
        }
        this.amount = amount;
        this.mutex = new Semaphore(1);
        currentSemaphore = new Semaphore(-(amount - 1));
    }

    /**
     * This method can be called by a thread to wait for the Sammelpunkt to be full. If the Sammelpunkt is full, all
     * waiting threads are released. If the thread is interrupted while waiting, the waiting is aborted and an
     * InterruptedException is thrown.
     * <p>
     * Do not use this in a loop, for it adds a new waiting thread each time this method is called.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public void waitForAll() throws InterruptedException {
        Semaphore mySemaphore = null;
        try {
            mutex.acquire();

            currentSemaphore.release();
            if (currentSemaphore.availablePermits() == 1) {
                currentSemaphore = new Semaphore(-(amount - 1));
            } else {
                mySemaphore = currentSemaphore;
            }
        } finally {
            mutex.release();
        }
        if (mySemaphore != null) {
            try {
                mySemaphore.acquire();
            } finally {
                mySemaphore.release();
            }
        }
    }
}
