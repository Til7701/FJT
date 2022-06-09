package de.holube.ex.ex06;


class StackTest {

    public static void main(String[] args) {
        test();
    }

    static void test() {
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < 2; i++) {
            PushingThread thread = new PushingThread(stack);
            thread.setName("PushingThread-" + i);
            thread.start();
            PoppingThread thread1 = new PoppingThread(stack);
            thread1.setName("PoppingThread-" + i);
            thread1.start();
            PeekingThread thread2 = new PeekingThread(stack);
            thread2.setName("PeekingThread-" + i);
            thread2.start();
        }
    }

    private static class PeekingThread extends Thread {

        private final Stack<Integer> stack;

        public PeekingThread(Stack<Integer> stack) {
            this.stack = stack;
        }

        @Override
        public void run() {
            while (true) {
                System.out.println(Thread.currentThread().getName() + " " + stack.peek());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class PushingThread extends Thread {

        private final Stack<Integer> stack;

        public PushingThread(Stack<Integer> stack) {
            this.stack = stack;
        }

        @Override
        public void run() {
            while (true) {
                Integer value = (int) (Math.random() * 100);
                stack.push(value);
                System.out.println(Thread.currentThread().getName() + " " + value);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class PoppingThread extends Thread {

        private final Stack<Integer> stack;

        public PoppingThread(Stack<Integer> stack) {
            this.stack = stack;
        }

        @Override
        public void run() {
            while (true) {
                Integer value = stack.pop();
                System.out.println(Thread.currentThread().getName() + " removed " + value);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
