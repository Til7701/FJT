package de.holube.ex.ex04;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Buffer<E> {

    private final Semaphore mutex;
    private final Semaphore readSemaphore;
    private final Semaphore writeSemaphore;

    private final List<E> list;
    private int readIndex = 0;
    private int writeIndex = 0;

    public Buffer(int size) {
        list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(null);
        }
        this.mutex = new Semaphore(1);
        this.readSemaphore = new Semaphore(0);
        this.writeSemaphore = new Semaphore(size);
    }

    public void put(E element) throws InterruptedException {
        try {
            writeSemaphore.acquire();
            try {
                mutex.acquire();
                list.set(writeIndex, element);
                writeIndex = (writeIndex + 1) % list.size();
                readSemaphore.release();
            } finally {
                mutex.release();
            }
        } catch (InterruptedException e) {
            writeSemaphore.release();
            throw e;
        }
    }

    public E get() throws InterruptedException {
        try {
            readSemaphore.acquire();
            try {
                mutex.acquire();
                E element = list.get(readIndex);
                readIndex = (readIndex + 1) % list.size();
                writeSemaphore.release();
                return element;
            } finally {
                mutex.release();
            }
        } catch (InterruptedException e) {
            readSemaphore.release();
            throw e;
        }
    }
}
