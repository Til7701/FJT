package de.holube.ex.ex06;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class VermittlerTest {

    @Test
    void test() {
        Vermittler<TestThread> vermittler = new Vermittler<>();

        TestThread[] threads = new TestThread[8];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new TestThread(vermittler);
            threads[i].setName("TestThread-" + i);
            threads[i].start();
        }

        for (TestThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (TestThread thread : threads) {
            for (int i = 0; i < thread.getReturnedThreads().size(); i++) {
                assertTrue(thread.getReturnedThreads().get(i).getReturnedThreads().contains(thread));
            }
        }
    }

    private static class TestThread extends Thread {

        private final Vermittler<TestThread> vermittler;
        private final List<TestThread> returnedThreads;

        public TestThread(Vermittler<TestThread> vermittler) {
            this.vermittler = vermittler;
            this.returnedThreads = new ArrayList<>();
        }

        @Override
        public void run() {
            for (int i = 0; i < 2; i++) {
                TestThread returnedValue = null;
                try {
                    returnedValue = vermittler.tauschen(this);
                    returnedThreads.add(returnedValue);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread " + getName() + " got " + returnedValue.getName());
            }
        }

        public List<TestThread> getReturnedThreads() {
            return returnedThreads;
        }
    }

}
