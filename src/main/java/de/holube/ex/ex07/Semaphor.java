package de.holube.ex.ex07;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Semaphor {

    private final BlockingDeque<Object> queue;

    /**
     * Creates a new Semaphor with the given amount of permits (>= 0).
     * If permits is <0 it is set to 0.
     *
     * @param permits the amount of permits
     */
    public Semaphor(int permits) {
        this.queue = new LinkedBlockingDeque<>();
        for (int i = 0; i < permits; i++) {
            queue.add(new Object());
        }
    }

    /**
     * Acquires a permit. Interruptions are ignored. However, if the thread is interrupted while waiting for a permit,
     * the interrupted status is set to true after leaving the method.
     */
    public void p() {
        Object o = null;
        boolean interrupted = false;

        while (o == null) {
            try {
                o = queue.take();
            } catch (InterruptedException e) {
                interrupted = true;
            }
        }

        if (interrupted)
            Thread.currentThread().interrupt();
    }

    /**
     * Releases a permit.
     */
    public void v() {
        queue.add(new Object());
    }

}
