package de.holube.ex.ex06;

class Producer extends Thread {
    private final Buffer<Integer> buffer;

    public Producer(Buffer<Integer> b) {
        buffer = b;

    }

    @Override
    public void run() {
        while (true) {
            int value = produceValue();
            buffer.put(value);
        }
    }

    private int produceValue() {
        return (int) (Math.random() * 100);
    }
}

class Consumer extends Thread {
    private final Buffer<Integer> buffer;

    public Consumer(Buffer<Integer> b) {
        buffer = b;
    }

    @Override
    public void run() {
        while (true) {
            int value = buffer.get();
            consumeValue(value);
        }
    }

    private void consumeValue(int value) {
        System.out.println("got " + value);
    }
}

public class ProduceConsume {
    public static void main(String[] args) {
        Buffer<Integer> b = new AtomicBuffer<>();
        for (int i = 0; i < 7; i++) {
            new Producer(b).start();
        }
        for (int i = 0; i < 5; i++) {
            new Consumer(b).start();
        }
    }
}
