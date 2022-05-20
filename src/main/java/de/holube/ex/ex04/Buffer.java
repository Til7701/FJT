package de.holube.ex.ex04;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Buffer<E> {

    private final Semaphore mutex;
    private final Semaphore readMutex;
    private final Semaphore writeMutex;

    private final List<E> list;
    private int readIndex = 0;
    private int writeIndex = 0;

    public Buffer(int size) {
        list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(null);
        }
        this.mutex = new Semaphore(1);
        this.readMutex = new Semaphore(0);
        this.writeMutex = new Semaphore(size);
    }

    public void put(E element) throws InterruptedException {
        try {
            writeMutex.acquire();
            mutex.acquire();
            list.set(writeIndex, element);
            writeIndex = (writeIndex + 1) % list.size();
            readMutex.release();
        } finally {
            mutex.release();
        }
    }

    public E get() throws InterruptedException {
        try {
            readMutex.acquire();
            mutex.acquire();
            E element = list.get(readIndex);
            readIndex = (readIndex + 1) % list.size();
            writeMutex.release();
            return element;
        } finally {
            mutex.release();
        }
    }
}
