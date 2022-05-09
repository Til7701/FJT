package de.holube.ex.ex03;

public class Sammelpunkt {

    private final int amount;
    private WaitingSession currentSession;

    public Sammelpunkt(int amount) {
        if (amount < 1) {
            amount = 1;
        }
        this.amount = amount;
        currentSession = new WaitingSession();
    }

    public synchronized void waitForAll() {
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
                    // ignore
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
