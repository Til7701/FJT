package de.holube.ex.ex03;

/**
 * This class provides a way to wait for a number of threads to finish.
 *
 * @author Tilman Holube
 */
public class Sammelpunkt {

    private final int amount;
    private WaitingSession currentSession;

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
        currentSession = new WaitingSession();
    }

    /**
     * This method can be called by a thread to wait for the Sammelpunkt to be full. If the Sammelpunkt is full, all
     * waiting threads are released. If the thread is interrupted while waiting, the waiting is aborted and an
     * InterruptedException is thrown.
     * <p>
     * Do not use this is a loop, for it adds a new waiting thread each time this method is called.
     * Use {@link #waitForAllWithoutInterrupt()} instead.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public synchronized void waitForAll() throws InterruptedException {
        currentSession.addWaitingThread();
        if (currentSession.getWaitingThreadsInSession() == amount) {
            notifyAll();
            currentSession = new WaitingSession();
        } else {
            WaitingSession mySession = currentSession;
            while (amount != mySession.getWaitingThreadsInSession()) {
                wait();
            }
        }
    }

    /**
     * This method can be called by a thread to wait for the Sammelpunkt to be full. If the Sammelpunkt is full, all
     * waiting threads are released. If the thread is interrupted while waiting, the waiting will continue and the
     * interrupted flag is set.
     */
    public synchronized void waitForAllWithoutInterrupt() {
        currentSession.addWaitingThread();
        if (currentSession.getWaitingThreadsInSession() == amount) {
            notifyAll();
            currentSession = new WaitingSession();
        } else {
            WaitingSession mySession = currentSession;
            while (amount != mySession.getWaitingThreadsInSession()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static class WaitingSession {

        private int waitingThreadsInSession = 0;

        public void addWaitingThread() {
            this.waitingThreadsInSession++;
        }

        public int getWaitingThreadsInSession() {
            return waitingThreadsInSession;
        }
    }
}
