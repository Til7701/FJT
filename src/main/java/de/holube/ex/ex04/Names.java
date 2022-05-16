package de.holube.ex.ex04;

import java.util.concurrent.locks.LockSupport;

import static java.lang.Thread.sleep;

public class Names {

    private static final int N = 10;

    public static void main(String[] args) throws InterruptedException {
        NameThread[] threads = new NameThread[N];
        for (int i = 0; i < N; i++) {
            threads[i] = new NameThread();
        }

        for (int i = 0; i < N; i++) {
            if (i == N - 1) {
                threads[i].setNext(threads[0]);
            } else {
                threads[i].setNext(threads[i + 1]);
            }
            threads[i].start();
        }
        sleep(2000);
        System.out.println("start");
        LockSupport.unpark(threads[0]);
    }

    private static class NameThread extends Thread {

        private volatile NameThread next;

        @Override
        public void run() {
            LockSupport.park();
            while (true) {
                System.out.println(getName());
                LockSupport.unpark(next);
                LockSupport.park();
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void setNext(NameThread next) {
            this.next = next;
        }
    }
}
