package de.holube.ex.ex02;

/**
 * A class used to use the {@link NameThread} class.
 *
 * @author Tilman Holube
 */
public class NameThreadMain {

    public static void main(String[] args) {
        NameThread[] threads = new NameThread[3];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new NameThread();
            threads[i].setDaemon(true);
        }

        for (NameThread nameThread : threads) {
            nameThread.start();
        }

        for (NameThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * A thread that prints its name.
     */
    static class NameThread extends Thread {

        /**
         * This lock is used to synchronize multiple instances of this class.
         */
        private static final Object LOCK = new Object();

        @Override
        public void run() {
            while (!interrupted()) {
                synchronized (LOCK) {
                    for (int i = 0; i < 10; i++) {
                        System.out.println(getName());
                    }
                }
            }
        }
    }

}
