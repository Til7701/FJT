package de.holube.ex.ex03;

class SetTest {

    public static void main(String[] args) {
        test();
    }

    static void test() {
        Set<Integer> set = new Set<>(10);
        for (int i = 0; i < 10; i++) {
            try {
                set.add(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 2; i++) {
            IteratingThread thread = new IteratingThread(set);
            thread.setName("1-IteratingThread " + i);
            thread.start();
        }

        for (int i = 0; i < 2; i++) {
            AddingThread thread = new AddingThread(set, i);
            thread.setName("AddingThread " + i);
            thread.start();
            RemovingThread thread1 = new RemovingThread(set);
            thread1.setName("RemovingThread " + i);
            thread1.start();
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("--------------------------");

        for (int i = 0; i < 2; i++) {
            IteratingThread thread = new IteratingThread(set);
            thread.setName("2-IteratingThread " + i);
            thread.start();
        }
    }

    private static class IteratingThread extends Thread {

        private final Set<Integer> set;

        public IteratingThread(Set<Integer> set) {
            this.set = set;
        }

        @Override
        public void run() {
            for (int integer : set) {
                System.out.println(Thread.currentThread().getName() + " " + integer);
                try {
                    Thread.sleep((long) (Math.random() * 2));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static class AddingThread extends Thread {

        private final Set<Integer> set;
        private final int value;

        public AddingThread(Set<Integer> set, int value) {
            this.set = set;
            this.value = value;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    set.add(value * i);
                    System.out.println(Thread.currentThread().getName() + " added " + value * i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class RemovingThread extends Thread {

        private final Set<Integer> set;

        public RemovingThread(Set<Integer> set) {
            this.set = set;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    set.remove(i);
                    System.out.println(Thread.currentThread().getName() + " removed " + i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
