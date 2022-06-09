package de.holube.ex.ex06;

public class VermittlerTest {

    public static void main(String[] args) {
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
            for (int i = 0; i < 1; i++) {
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
