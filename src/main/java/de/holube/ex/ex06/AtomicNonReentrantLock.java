package de.holube.ex.ex06;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicNonReentrantLock implements Lock {

    private final AtomicReference<Thread> owner = new AtomicReference<>();

    @Override
    public void lock() {
        while (!owner.compareAndSet(null, Thread.currentThread())) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void unlock() throws IllegalMonitorStateException {
        if (!owner.compareAndSet(Thread.currentThread(), null)) {
            throw new IllegalMonitorStateException();
        }
    }

}
