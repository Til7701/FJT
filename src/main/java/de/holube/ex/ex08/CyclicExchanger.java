package de.holube.ex.ex08;

public class CyclicExchanger<V> {

    private int next;
    private Object[] data;
    private final int number;

    public CyclicExchanger(int number) {
        if (number < 2) {
            throw new IllegalArgumentException("number has to be greater than 1");
        }
        this.number = number;
        this.data = new Object[number];
        next = 0;
    }

    @SuppressWarnings("unchecked")
    public synchronized V exchange(V value) throws InterruptedException {
        if (next == number - 1) {
            data[next] = value;
            next = 0;
            notifyAll();
            return (V) data[number - 2];
        } else {
            data[next] = value;
            int index = next;
            next++;
            wait();
            int prev = Math.floorMod(index - 1, number);
            return (V) data[prev];
        }
    }

    public static void main(String[] args) {
        System.out.println("main start");
        CyclicExchanger<String> exchanger = new CyclicExchanger<>(5);

        Runnable operations = () -> {
//			System.out.println(Thread.currentThread().getName() + " started!");
            try {
                String othername = exchanger.exchange(Thread.currentThread().getName());
                System.out.println(Thread.currentThread().getName() + " gruesst " + othername);
//				Thread.sleep((long) (Math.random() * 2000));
            } catch (InterruptedException e1) {
            }
//			System.out.println(Thread.currentThread() + " finished!");
        };

        for (int i = 0; i < 30; i++) {
            new Thread(operations).start();
        }

        System.out.println("main end");
    }
}
