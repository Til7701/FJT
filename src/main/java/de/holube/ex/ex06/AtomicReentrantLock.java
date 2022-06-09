package de.holube.ex.ex06;

import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicReentrantLock implements Lock {

    private final AtomicStampedReference<Thread> owner = new AtomicStampedReference<>(null, 0);

    @Override
    public void lock() {
        int stamp = owner.getStamp();
        if (owner.compareAndSet(Thread.currentThread(), Thread.currentThread(), stamp, stamp + 1)) {
            return;
        }

        while (!owner.compareAndSet(null, Thread.currentThread(), 0, 1)) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        owner.set(Thread.currentThread(), 1);
    }

    @Override
    public void unlock() throws IllegalMonitorStateException {
        int stamp = owner.getStamp();
        if (!owner.compareAndSet(Thread.currentThread(), Thread.currentThread(), stamp, stamp - 1)) {
            throw new IllegalMonitorStateException();
        }
        if (owner.getStamp() == 0) {
            owner.set(null, 0);
        }
    }

}
