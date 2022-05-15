package de.holube.ex.ex03;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ThreadPoolTest {

    public static void main(String[] args) {
        executeTest();
    }

    static void executeTest() {
        ThreadPool threadPool = new ThreadPool(4);

        TestThread[] threads = new TestThread[5];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new TestThread(threadPool, i);
        }
        for (TestThread thread : threads) {
            thread.start();
        }

        for (TestThread thread : threads) {
            while (thread.isAlive()) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        while (threadPool.isWorking()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threadPool.shutdown();
    }

    private static class TestThread extends Thread {

        private final ThreadPool threadPool;
        private final int value;

        public TestThread(ThreadPool threadPool, int value) {
            this.threadPool = threadPool;
            this.value = value;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep((long) (Math.random() * 2));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                threadPool.execute(() -> {
                    System.out.println(value + " is started in thread " + Thread.currentThread().getName());
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }
    }

    @Test
    void mockedTest() {
        ThreadPool threadPool = new ThreadPool(4);

        final int THREADS = 5;
        final int RUNNABLES = 10;
        MockingThread[] threads = new MockingThread[THREADS];
        Runnable[] runnables = new Runnable[RUNNABLES];

        for (int i = 0; i < runnables.length; i++) {
            runnables[i] = mock(Runnable.class);
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new MockingThread(threadPool, runnables);
        }
        for (MockingThread thread : threads) {
            thread.start();
        }

        for (MockingThread thread : threads) {
            while (thread.isAlive()) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        while (threadPool.isWorking()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threadPool.shutdown();

        for (Runnable runnable : runnables) {
            verify(runnable, times(THREADS)).run();
        }
    }

    private static class MockingThread extends Thread {

        private final ThreadPool threadPool;
        private final Runnable[] runnables;

        public MockingThread(ThreadPool threadPool, Runnable[] runnables) {
            this.threadPool = threadPool;
            this.runnables = runnables;
        }

        @Override
        public void run() {
            for (Runnable runnable : runnables) {
                threadPool.execute(runnable);
            }
        }
    }

}
