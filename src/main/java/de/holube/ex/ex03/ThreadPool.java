package de.holube.ex.ex03;

import java.util.ArrayDeque;
import java.util.Queue;

public class ThreadPool {

    private final PoolThread[] threads;
    private final Queue<Runnable> tasks;
    private final Object syncObject = new Object();

    public ThreadPool(int poolSize) {
        threads = new PoolThread[poolSize];
        for (int i = 0; i < poolSize; i++) {
            threads[i] = new PoolThread(this);
            threads[i].start();
        }

        tasks = new ArrayDeque<>();
    }

    public void execute(Runnable task) {
        synchronized (syncObject) {
            tasks.add(task);
            syncObject.notifyAll();
        }
    }

    public void shutdown() {
        for (PoolThread thread : threads) {
            thread.running = false;
            thread.interrupt();
        }
    }

    private static class PoolThread extends Thread {

        private final ThreadPool pool;
        private boolean running;

        public PoolThread(ThreadPool pool) {
            this.pool = pool;
            running = true;
        }

        @Override
        public void run() {
            while (!interrupted() && running) {
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
