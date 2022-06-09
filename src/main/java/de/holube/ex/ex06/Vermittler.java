package de.holube.ex.ex06;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class Vermittler<V> {

    private final AtomicMarkableReference<V> data = new AtomicMarkableReference<>(null, false);
    private final AtomicMarkableReference<V> data2 = new AtomicMarkableReference<>(null, false);

    //FIXME not done yet
    public V tauschen(V inValue) throws InterruptedException {
        if (data.compareAndSet(null, inValue, false, true)) { // first thread setting value
            boolean[] mark = {false};
            V value2 = data2.get(mark);
            while (!data2.compareAndSet(value2, null, true, false)) { // frist thread waiting for second thread to set value
                //Thread.yield();
                data2.get(mark);
                if (Thread.currentThread().isInterrupted()) {
                    data.set(null, false);
                    throw new InterruptedException();
                }
            }
            return value2;
        }

        // second thread setting value
        boolean[] mark = {false};
        V value = data.get(mark);
        while (!data.compareAndSet(value, inValue, mark[0], false)) { // second thread waiting for first thread to set value
            //Thread.yield();
            data.get(mark);
            if (Thread.currentThread().isInterrupted()) throw new InterruptedException();
        }
        return value;
    }

}
