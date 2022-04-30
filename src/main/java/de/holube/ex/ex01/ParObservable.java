package de.holube.ex.ex01;

import java.util.Observable;
import java.util.Observer;

public class ParObservable extends Observable {

    @Override
    public void notifyObservers(Object arg) {
        Object[] arrLocal;

        synchronized (this) {
            if (!this.hasChanged())
                return;
            arrLocal =
                    clearChanged();
        }

        for (int i = arrLocal.length - 1; i >= 0; i--)
            ((Observer) arrLocal[i]).update(this, arg);
    }

}
