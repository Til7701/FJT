package de.holube.ex.ex09;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

class AliceA4 extends Thread {

    private BlockingQueue<String> messages = new LinkedBlockingQueue<>();

    @Override
    public void run() {
        while (!interrupted()) {
            try {
                String message = messages.take();
                System.out.println(message);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }

    public void send(String message) {
        try {
            messages.put(message);
        } catch (InterruptedException e) {
        }
    }

}

class BobA4 extends Thread {

    private AliceA4 alice;

    public BobA4(String name, AliceA4 alice) {
        super(name);
        this.alice = alice;
    }

    @Override
    public void run() {
        while (!interrupted()) {
            String message = "Greetings from " + getName() + "!";
            alice.send(message);
            try {
                Thread.sleep(ThreadLocalRandom.current().nextLong(1000));
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}

public class BobAliceA4 {

    public static void main(String[] args) throws Exception {
        AliceA4 alice = new AliceA4();
        alice.start();
        for (int i = 1; i < 100; i++) {
            Thread bob = new BobA4("Bob-" + i, alice);
            bob.start();
            Thread.sleep(ThreadLocalRandom.current().nextLong(100));
        }

    }

}
