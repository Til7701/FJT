package de.holube.ex.ex06;

public class AtomicReentrantLockTest {

    public static void main(String[] args) {
        Lock lock = new AtomicReentrantLock();
        Thread t1 = new Thread(() -> {
            lock.lock();
            System.out.println("t1 locked");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
            System.out.println("t1 unlocked");
        });

        Thread t2 = new Thread(() -> {
            lock.lock();
            System.out.println("t2 locked");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
            System.out.println("t2 unlocked");
        });

        Thread t3 = new Thread(() -> {
            lock.lock();
            System.out.println("t3 locked");
            try {
                Thread.sleep(1000);
                System.out.println("t3 locking again");
                lock.lock();
                System.out.println("t3 locked again");
                Thread.sleep(1000);
                lock.unlock();
                System.out.println("t3 unlocked again");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
            System.out.println("t3 unlocked");
        });
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t3.start();

        try {
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("main finished");
    }
}
