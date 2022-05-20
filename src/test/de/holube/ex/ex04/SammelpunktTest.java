package de.holube.ex.ex04;

class SammelpunktTest {

    public static void main(String[] args) {
        waitForAllTest();
    }

    static void waitForAllTest() {
        Sammelpunkt sammelpunkt = new Sammelpunkt(5);

        TestThread[] threads = new TestThread[9];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new TestThread(sammelpunkt);
            threads[i].setName("TestThread " + i);
        }
        for (TestThread thread : threads) {
            thread.start();
        }
    }

    private static class TestThread extends Thread {

        private final Sammelpunkt sammelpunkt;

        public TestThread(Sammelpunkt sammelpunkt) {
            this.sammelpunkt = sammelpunkt;
        }

        @Override
        public void run() {
            System.out.println(getName() + " started");
            try {
                System.out.println(Thread.currentThread().getName() + ": wartet auf Sammelpunkt");
                sammelpunkt.waitForAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(getName() + " finished");
        }
    }
}
