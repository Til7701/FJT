package de.holube.ex.ex04;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * A class to represent a thread-safe buffer.
 *
 * @param <E> the type of the elements in the buffer
 * @author Tilman Holube
 */
public class Buffer<E> {

    private final Semaphore mutex;
    private final Semaphore readSemaphore;
    private final Semaphore writeSemaphore;

    private final List<E> list;
    private int readIndex = 0;
    private int writeIndex = 0;

    /**
     * Creates a new buffer with the given initial capacity.
     *
     * @param size the initial capacity
     */
    public Buffer(int size) {
        list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(null);
        }
        this.mutex = new Semaphore(1);
        this.readSemaphore = new Semaphore(0);
        this.writeSemaphore = new Semaphore(size);
    }

    /**
     * Adds the given element to this buffer. The calling thread is blocked until the buffer is not full.
     *
     * @param element the element to add
     * @throws InterruptedException if the thread was interrupted while waiting
     */
    public void put(E element) throws InterruptedException {
        writeSemaphore.acquire();
        try {
            mutex.acquire();
            list.set(writeIndex, element);
            writeIndex = (writeIndex + 1) % list.size();
            readSemaphore.release();
        } catch (InterruptedException e) {
            writeSemaphore.release();
            throw e;
        } finally {
            mutex.release();
        }
    }

    /**
     * Returns the next element from this buffer. The calling thread is blocked until the buffer is not empty.
     *
     * @return the next element from this buffer
     * @throws InterruptedException if the thread was interrupted while waiting
     */
    public E get() throws InterruptedException {
        readSemaphore.acquire();
        try {
            mutex.acquire();
            E element = list.get(readIndex);
            readIndex = (readIndex + 1) % list.size();
            writeSemaphore.release();
            return element;
        } catch (InterruptedException e) {
            readSemaphore.release();
            throw e;
        } finally {
            mutex.release();
        }
    }
}
