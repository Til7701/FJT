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
