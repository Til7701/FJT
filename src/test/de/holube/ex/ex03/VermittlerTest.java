package de.holube.ex.ex03;

import org.junit.jupiter.api.Test;

class VermittlerTest {

    @Test
    void tauschenTest() {
        Vermittler<String> vermittler = new Vermittler<>();

        TestThread[] threads = new TestThread[5];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new TestThread(vermittler, "Thread " + i);
            threads[i].setName("TestThread " + i);
            threads[i].start();
        }
    }

    private static class TestThread extends Thread {

        private final Vermittler<String> vermittler;
        private final String value;

        public TestThread(Vermittler<String> vermittler, String value) {
            this.vermittler = vermittler;
            this.value = value;
        }

        @Override
        public void run() {
            try {
                Thread.sleep((long) (Math.random() * 10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < 2; i++) {
                String returnedValue = "NOT SWAPPED YET";
                try {
                    returnedValue = vermittler.tauschen(value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread " + getName() + " got " + returnedValue);
            }
        }
    }

}
