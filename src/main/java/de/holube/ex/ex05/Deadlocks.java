package de.holube.ex.ex05;

public class Deadlocks {

    public static void main(String[] args) {
        new Deadlocks();
    }

    Deadlocks() {
        new Thread1().start();
        new Thread2().start();
        new Thread3().start();
    }

    private int a = 1;
    private int b = 3;
    private int c = 5;
    private Object aSync = new Object();
    private Object bSync = new Object();
    private Object cSync = new Object();

    class Thread1 extends Thread {
        @Override
        public void run() {
            synchronized (aSync) {
                synchronized (bSync) {
                    synchronized (cSync) {
                        // do something
                        a = b + c;
                        System.out.println(a);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        }
    }

    class Thread2 extends Thread {
        @Override
        public void run() {
            synchronized (bSync) {
                synchronized (cSync) {
                    synchronized (aSync) {
                        // do something
                        b = c + a;
                        System.out.println(b);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        }
    }

    class Thread3 extends Thread {
        @Override
        public void run() {
            synchronized (cSync) {
                synchronized (aSync) {
                    synchronized (bSync) {
                        // do something
                        c = a + b;
                        System.out.println(c);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        }
    }

}
