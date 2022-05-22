package de.holube.ex.ex04;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.LockSupport;

public class Names {

    private static final int N = 10;

    public static void main(String[] args) {
        Semaphore startedSemaphore = new Semaphore(-(N - 1));

        // creating threads
        NameThread[] threads = new NameThread[N];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new NameThread(startedSemaphore);
        }

        // setting next and starting threads
        for (int i = 0; i < threads.length; i++) {
            threads[i].setNext(threads[(i + 1) % threads.length]);
            threads[i].start();
        }

        // waiting for all threads to start
        boolean allStarted = false;
        while (!allStarted) {
            try {
                startedSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
                // ignore
            }
            allStarted = true;
            for (NameThread thread : threads) {
                if (!thread.isAlive()) {
                    allStarted = false;
                    break;
                }
            }
        }

        // telling the first to start
        threads[0].setAllowedToContinue(true);
        LockSupport.unpark(threads[0]);
    }

    private static class NameThread extends Thread {

        private final Semaphore startedSemaphore;
        private NameThread next;
        private volatile boolean allowedToContinue;

        public NameThread(Semaphore startedSemaphore) {
            this.startedSemaphore = startedSemaphore;
        }

        @Override
        public void run() {
            startedSemaphore.release();
            while (!interrupted()) {
                while (!allowedToContinue) {
                    LockSupport.park();
                }
                setAllowedToContinue(false);
                System.out.println(getName());
                try {
                    sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                next.setAllowedToContinue(true);
                LockSupport.unpark(next);
            }
        }

        public synchronized void setNext(NameThread next) {
            this.next = next;
        }

        public synchronized void setAllowedToContinue(boolean allowedToContinue) {
            this.allowedToContinue = allowedToContinue;
        }
    }
}
