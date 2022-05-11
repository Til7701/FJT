package de.holube.ex.ex03;

import org.junit.jupiter.api.Test;

class SammelpunktTest {

    @Test
    void waitForAllTest() {
        Sammelpunkt sammelpunkt = new Sammelpunkt(5);

        TestThread[] threads = new TestThread[10];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new TestThread(sammelpunkt);
            threads[i].setName("TestThread " + i);
            threads[i].start();
        }
    }

    private static class TestThread extends Thread {

        private final Sammelpunkt sammelpunkt;

        public TestThread(Sammelpunkt sammelpunkt) {
            this.sammelpunkt = sammelpunkt;
        }

        @Override
        public void run() {
            try {
                sammelpunkt.waitForAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread " + getName() + " finished");
        }
    }
}
