package de.holube.ex.ex04;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class BufferL {
    private boolean available = false;
    private int data;
    private final Lock lock = new MyLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public void put(int value) {
        lock.lock();
        try {
            while (available) {
                notFull.awaitUninterruptibly();
            }
            data = value;
            available = true;
            notEmpty.signal(); // kein signalAll erforderlich
        } finally {
            lock.unlock();
        }
    }

    public int get() {
        lock.lock();
        try {
            while (!available) {
                notEmpty.awaitUninterruptibly();
            }
            available = false;
            notFull.signal(); // kein signalAll erforderlich
            return data;
        } finally {
            lock.unlock();
        }
    }
}
