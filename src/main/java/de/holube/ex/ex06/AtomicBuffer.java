package de.holube.ex.ex06;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class AtomicBuffer<V> implements Buffer<V> {

    private final AtomicMarkableReference<V> data = new AtomicMarkableReference<>(null, false);

    @Override
    public void put(V value) {
        while (!data.compareAndSet(null, value, false, true)) {
            //Thread.yield();
        }
    }

    @Override
    public V get() {
        V value = data.getReference();
        while (!data.compareAndSet(value, null, true, false)) {
            value = data.getReference();
            //Thread.yield();
        }
        return value;
    }

}
