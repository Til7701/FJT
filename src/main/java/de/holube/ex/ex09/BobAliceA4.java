package de.holube.ex.ex09;

import java.util.concurrent.*;

class AliceA4 extends Thread {

    private final BlockingQueue<String> messages = new LinkedBlockingQueue<>();

    private final ScheduledExecutorService executorService;

    public AliceA4(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void run() {
        executorService.schedule(this::interrupt, 30, TimeUnit.SECONDS);
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

class BobA4 {

    public BobA4(String name, AliceA4 alice, ScheduledExecutorService executorService) {
        executorService.scheduleAtFixedRate(() -> alice.send("Greetings from " + name + "!"), 10, 2, TimeUnit.SECONDS);
        executorService.schedule(executorService::shutdown, 60, TimeUnit.SECONDS);
    }

}

public class BobAliceA4 {

    public static void main(String[] args) throws Exception {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        AliceA4 alice = new AliceA4(executor);
        alice.start();
        for (int i = 1; i < 100; i++) {
            new BobA4("Bob-" + i, alice, executor);
            Thread.sleep(ThreadLocalRandom.current().nextLong(100));
        }
    }

}
