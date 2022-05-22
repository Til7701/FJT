package de.holube.ex.ex04;

import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * My own implementation of a lock.
 *
 * @author Tilman Holube
 */
public class MyLock implements Lock {

    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore lock = new Semaphore(1);
    private Thread owner;

    @Override
    public void lock() {
        boolean acquired = false;
        while (!acquired) {
            try {
                lockInterruptibly();
                acquired = true;
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        mutex.acquire();
        try {
            lock.acquire();
            owner = Thread.currentThread();
        } finally {
            mutex.release();
        }
    }

    @Override
    public boolean tryLock() {
        boolean acquired = lock.tryAcquire();
        if (acquired) {
            try {
                mutex.acquire();
                owner = Thread.currentThread();
            } catch (InterruptedException e) {
                // ignore
            } finally {
                mutex.release();
            }
        }
        return acquired;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        boolean acquired = lock.tryAcquire(time, unit);
        if (acquired) {
            try {
                mutex.acquire();
                owner = Thread.currentThread();
            } catch (InterruptedException e) {
                // ignore
            } finally {
                mutex.release();
            }
        }
        return acquired;
    }

    @Override
    public void unlock() {
        try {
            mutex.acquire();
            if (Thread.currentThread().equals(owner)) {
                owner = null;
                lock.release();
            }
        } catch (InterruptedException e) {
            // ignore
        } finally {
            mutex.release();
        }
    }

    @Override
    public Condition newCondition() {
        return new MyCondition(this);
    }

    public static class MyCondition implements Condition {

        private final MyLock lock;

        private final Semaphore mutex = new Semaphore(1);
        private final Semaphore condition = new Semaphore(0);
        private int waitingThreads = 0;

        public MyCondition(MyLock lock) {
            this.lock = lock;
        }

        @Override
        public void await() throws InterruptedException {
            lock.unlock();
            try {
                mutex.acquire();
                waitingThreads++;
            } finally {
                mutex.release();
            }
            try {
                condition.acquire();
            } finally {
                boolean removed = false;
                while (!removed) {
                    try {
                        mutex.acquire();
                        waitingThreads--;
                        removed = true;
                    } catch (InterruptedException e1) {
                        // ignore
                    } finally {
                        mutex.release();
                    }
                }
            }
        }

        @Override
        public void awaitUninterruptibly() {
            boolean acquired = false;
            while (!acquired) {
                try {
                    await();
                    acquired = true;
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }

        @Override
        public long awaitNanos(long nanosTimeout) throws InterruptedException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean await(long time, TimeUnit unit) throws InterruptedException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean awaitUntil(Date deadline) throws InterruptedException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void signal() {
            condition.release();
        }

        @Override
        public void signalAll() {
            boolean acquired = false;
            while (!acquired) {
                try {
                    mutex.acquire();
                    condition.release(waitingThreads);
                    acquired = true;
                } catch (InterruptedException e) {
                    // ignore
                } finally {
                    mutex.release();
                }
            }
        }
    }
}
