package de.holube.ex.ex05;

import java.util.ArrayList;
import java.util.List;

public class FairLock {
    private boolean isLocked = false;
    private Thread lockingThread = null;
    private List<Object> waitingThreads = new ArrayList<>();

    public void lock() throws InterruptedException {
        Object queueObject = new Object();

        synchronized (this) {
            waitingThreads.add(queueObject);

            while (isLocked || waitingThreads.get(0) != queueObject) {

                synchronized (queueObject) {
                    try {
                        queueObject.wait();
                    } catch (InterruptedException e) {
                        waitingThreads.remove(queueObject);
                        throw e;
                    }
                }
            }
            waitingThreads.remove(queueObject);
            isLocked = true;
            lockingThread = Thread.currentThread();
        }
    }

    public synchronized void unlock() {
        if (this.lockingThread != Thread.currentThread()) {
            throw new IllegalMonitorStateException("Calling thread has not locked this lock");
        }
        isLocked = false;
        lockingThread = null;
        if (waitingThreads.size() > 0) {
            Object queueObject = waitingThreads.get(0);
            synchronized (queueObject) {
                queueObject.notify();
            }
        }
    }
}