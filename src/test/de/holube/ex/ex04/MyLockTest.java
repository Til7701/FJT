package de.holube.ex.ex04;

public class MyLockTest {

    public static void main(String[] args) {
        test();
    }

    static void test() {
        BufferL buffer = new BufferL();

        for (int i = 0; i < 2; i++) {
            AddingThread thread = new AddingThread(buffer, i);
            thread.setName("AddingThread " + i);
            thread.start();
            RemovingThread thread1 = new RemovingThread(buffer);
            thread1.setName("RemovingThread " + i);
            thread1.start();
        }
    }

    private static class AddingThread extends Thread {

        private final BufferL buffer;
        private final int value;

        public AddingThread(BufferL buffer, int value) {
            this.buffer = buffer;
            this.value = value;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                buffer.put(value);
                System.out.println(Thread.currentThread().getName() + " added " + value);
            }
        }
    }

    private static class RemovingThread extends Thread {

        private final BufferL buffer;

        public RemovingThread(BufferL buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + " removed " + buffer.get());
            }
        }
    }

}
