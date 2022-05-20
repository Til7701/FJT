package de.holube.ex.ex04;

public class BufferTest {

    public static void main(String[] args) {
        test();
    }

    static void test() {
        Buffer<String> buffer = new Buffer<>(3);

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

        private final Buffer<String> buffer;
        private final int value;

        public AddingThread(Buffer<String> buffer, int value) {
            this.buffer = buffer;
            this.value = value;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    String toAdd = String.format("[%d%s]", i, value);
                    buffer.put(toAdd);
                    System.out.println(Thread.currentThread().getName() + " added " + toAdd);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class RemovingThread extends Thread {

        private final Buffer<String> buffer;

        public RemovingThread(Buffer<String> buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + " removed " + buffer.get());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
