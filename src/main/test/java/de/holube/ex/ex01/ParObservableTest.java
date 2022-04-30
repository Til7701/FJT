package de.holube.ex.ex01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParObservableTest {

    private ParObservable defaultParObservable;
    private ParObservable.Observer[] observers;

    @BeforeEach
    private void prepareObservable() {
        defaultParObservable = new ParObservable();
        observers = new ParObservable.Observer[10];
        for (int i = 0; i < observers.length; i++) {
            observers[i] = mock(ParObservable.Observer.class);
            defaultParObservable.addObserver(observers[i]);
        }
    }

    @Test
    void addObserverTest() {
        assertEquals(10, defaultParObservable.countObservers());
        defaultParObservable.addObserver(observers[0]);
        assertEquals(10, defaultParObservable.countObservers());

        assertThrows(NullPointerException.class, () -> defaultParObservable.addObserver(null));
    }

    @Test
    void deleteObserverTest() {
        assertEquals(10, defaultParObservable.countObservers());
        defaultParObservable.deleteObserver(observers[0]);
        assertEquals(9, defaultParObservable.countObservers());
        defaultParObservable.deleteObserver(observers[0]);
        assertEquals(9, defaultParObservable.countObservers());

        defaultParObservable.notifyObservers();
        verify(observers[0], never()).update(defaultParObservable, null);
    }

    @Test
    void notifyObserversTest() {
        Object arg = new Object();
        defaultParObservable.setChanged();
        defaultParObservable.notifyObservers(arg);
        for (ParObservable.Observer observer : observers) {
            verify(observer).update(defaultParObservable, arg);
        }
    }

    @Test
    void deleteObserversTest() {
        defaultParObservable.deleteObservers();
        assertEquals(0, defaultParObservable.countObservers());
    }

    @Test
    void changeTest() {
        assertFalse(defaultParObservable.hasChanged());
        defaultParObservable.setChanged();
        assertTrue(defaultParObservable.hasChanged());
        defaultParObservable.clearChanged();
        assertFalse(defaultParObservable.hasChanged());
        defaultParObservable.setChanged();
        defaultParObservable.notifyObservers();
        assertFalse(defaultParObservable.hasChanged());
    }

    /**
     * Kleines Testprogramm für die Aufgabe.
     */
    @Test
    void test() {
        record ObserverImpl(int i) implements ParObservable.Observer {
            @Override
            public void update(ParObservable observable, Object arg) {
                System.out.printf("update %d, %s %n", i, arg);
            }
        }

        ParObservable parObservable = new ParObservable();
        for (int i = 0; i < 10; i++) {
            parObservable.addObserver(new ObserverImpl(i));
        }

        parObservable.setChanged();
        parObservable.notifyObservers("hi");
    }

}
