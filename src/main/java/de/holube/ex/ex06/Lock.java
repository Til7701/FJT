package de.holube.ex.ex06;

public interface Lock {

    // Acquires the lock. If the lock is not available then the current thread
    // becomes disabled for thread scheduling purposes and waits until the
    // lock has been acquired.
    void lock();

    // Releases the lock. If the holder of the lock is not the current thread an
    // IllegalMonitorStateException is thrown
    void unlock() throws IllegalMonitorStateException;

}
