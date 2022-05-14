package de.holube.ex.ex03;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A thread pool.
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

    private static class PoolThread extends Thread {

        private final ThreadPool pool;
        private volatile boolean allowedToRun;

        public PoolThread(ThreadPool pool) {
            this.pool = pool;
            allowedToRun = true;
        }

        @Override
        public void run() {
            while (!interrupted() && allowedToRun) {
                Runnable task;
                synchronized (pool.syncObject) {
                    while (pool.tasks.isEmpty()) {
                        try {
                            pool.syncObject.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                    task = pool.tasks.poll();
                }
                if (task != null) task.run();
            }
        }
    }
}
