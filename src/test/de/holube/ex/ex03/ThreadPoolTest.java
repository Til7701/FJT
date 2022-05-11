package de.holube.ex.ex03;

import org.junit.jupiter.api.Test;

class ThreadPoolTest {

    @Test
    void executeTest() {
        ThreadPool threadPool = new ThreadPool(2);

        TestThread[] threads = new TestThread[5];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new TestThread(threadPool, "a".repeat(i + 1));
        }
        for (TestThread thread : threads) {
            thread.start();
        }
    }

    private static class TestThread extends Thread {

        private final ThreadPool threadPool;
        private final String value;

        public TestThread(ThreadPool threadPool, String value) {
            this.threadPool = threadPool;
            this.value = value;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                threadPool.execute(() -> {
                    System.out.println(value + "start");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                    System.out.println(value + "end");
                });
            }
        }
    }

}
