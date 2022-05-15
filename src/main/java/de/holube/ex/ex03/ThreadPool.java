package de.holube.ex.ex03;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A thread pool.
 *
 * @author Tilman Holube
 */
public class ThreadPool {

    private final PoolThread[] threads;
    private final Queue<Runnable> tasks;
    private final Object syncObject = new Object();

    /**
     * Creates a new thread pool with the given number of threads.
     *
     * @param poolSize the number of threads in the pool
     */
    public ThreadPool(int poolSize) {
        threads = new PoolThread[poolSize];
        for (int i = 0; i < poolSize; i++) {
            threads[i] = new PoolThread(this);
            threads[i].setName("PoolThread " + i);
            threads[i].start();
        }

        tasks = new ArrayDeque<>();
    }

    /**
     * Adds a task to the pool.
     *
     * @param task the task to add
     */
    public void execute(Runnable task) {
        synchronized (syncObject) {
            tasks.add(task);
            syncObject.notifyAll();
        }
    }

    /**
     * Shuts down the pool. This method interrupts all threads in the pool. Running tasks will be finished.
     * Queued tasks will be discarded.
     */
    public void shutdown() {
        for (PoolThread thread : threads) {
            thread.allowedToRun = false;
            thread.interrupt();
        }
    }

    /**
     * This method determines if the pool is working. A pool is working if at least one thread is executing a task or
     * there is a task in the queue.
     *
     * @return true if the pool is working, false otherwise
     */
    public boolean isWorking() {
        synchronized (syncObject) {
            for (PoolThread thread : threads) {
                if (thread.isWorking()) {
                    return true;
                }
            }
            return !tasks.isEmpty();
        }
    }

    private static class PoolThread extends Thread {

        private final ThreadPool pool;
        private volatile boolean allowedToRun;
        private volatile boolean working;

        public PoolThread(ThreadPool pool) {
            this.pool = pool;
            allowedToRun = true;
        }

        @Override
        public void run() {
            while (!interrupted() && allowedToRun) {
                Runnable task;
                synchronized (pool.syncObject) {
                    working = false;
                    while (pool.tasks.isEmpty()) {
                        try {
                            pool.syncObject.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                    working = true;
                    task = pool.tasks.poll();
                }
                if (task != null) task.run();
            }
        }

        public boolean isWorking() {
            return working;
        }
    }
}
