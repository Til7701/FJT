package de.holube.ex.ex03;

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

}
