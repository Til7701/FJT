package de.holube.ex.ex05;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MVCDemo {

    public static void main(String[] args) {
        Number number;
        number = new MyNumber();
//      number = new NumberSync();
//		number = new NumberRWLock();
        number = new NumberRWLock2();
        GUI gui = new GUI(number);
        gui.setBounds(10, 10, 400, 800);
        gui.setVisible(true);
    }

}

abstract class Number extends Observable {
    protected int number;

    public Number() {
        this.number = 0;
    }

    public abstract int getNumber();

    public abstract void setNumber(int number);
}

class MyNumber extends Number {

    private final Semaphore mutex = new Semaphore(1, true);
    private final Semaphore waiting = new Semaphore(1, true);

    @Override
    public int getNumber() {
        mutex.acquireUninterruptibly();
        try {
            return number;
        } finally {
            mutex.release();
        }
    }

    @Override
    public void setNumber(int number) {
        waiting.acquireUninterruptibly();
        try {
            mutex.acquireUninterruptibly();
            try {
                this.number = number;
                setChanged();
            } finally {
                mutex.release();
            }
            notifyObservers();
        } finally {
            waiting.release();
        }
    }
}

class NumberSync extends Number {

    public synchronized int getNumber() {
        return number;
    }

    public synchronized void setNumber(int number) {
        // es soll sicher gestellt sein, dass alle Observer ihre
        // Aktion durchgefuehrt haben, bevor ein weiterer Thread eine erneute Aenderung
        // der Number durchfuehren kann
        this.number = number;
        setChanged();
        notifyObservers(); // greifen nur lesend auf Number zu!
    }
}

class NumberRWLock extends Number {
    private ReadWriteLock rwLock;
    private Lock rLock;
    private Lock wLock;

    public NumberRWLock() {
        super();
        rwLock = new ReentrantReadWriteLock();
        rLock = rwLock.readLock();
        wLock = rwLock.writeLock();
    }

    public int getNumber() {
        rLock.lock();
        try {
            return number;
        } finally {
            rLock.unlock();
        }
    }

    public void setNumber(int number) {
        // es soll sicher gestellt sein, dass alle Observer ihre
        // Aktion durchgefuehrt haben, bevor ein weiterer Thread eine erneute Aenderung
        // der Number durchfuehren kann
        wLock.lock();
        try {
            this.number = number;
            setChanged();
            rLock.lock(); // downgrade
        } finally {
            wLock.unlock();
        }
        try {
            notifyObservers(); // greifen nur lesend auf Number zu!
        } finally {
            rLock.unlock();
        }
    }
}

class NumberRWLock2 extends Number {
    private ReadWriteLock rwLock;
    private Lock rLock;
    private Lock wLock;

    public NumberRWLock2() {
        super();
        rwLock = new ReentrantReadWriteLock();
        rLock = rwLock.readLock();
        wLock = rwLock.writeLock();
    }

    public int getNumber() {
        rLock.lock();
        try {
            return number;
        } finally {
            rLock.unlock();
        }
    }

    public void setNumber(int number) {
        // es soll sicher gestellt sein, dass alle Observer ihre
        // Aktion durchgefuehrt haben, bevor ein weiterer Thread eine erneute Aenderung
        // der Number durchfuehren kann
        while (!wLock.tryLock()) {
            Thread.yield();
        }
        try {
            this.number = number;
            setChanged();
            rLock.lock(); // downgrade
        } finally {
            wLock.unlock();
        }
        try {
            notifyObservers(); // greifen nur lesend auf Number zu!
        } finally {
            rLock.unlock();
        }

    }
}

class GUI extends JFrame {
    public GUI(Number number) {
        super("Demo");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JButton start = new JButton("Start 1");
        start.addActionListener(new ClickListener(number));
        MyLabel label = new MyLabel(number);
        this.setLayout(new GridLayout(2, 1));
        this.add(start);
        this.add(label);
    }
}

class MyLabel extends JLabel implements Observer {

    private Number number;

    public MyLabel(Number number) {
        super(Integer.toString(number.getNumber()));
        this.number = number;
        number.addObserver(this);
        this.setOpaque(true);
        repaintYou();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!SwingUtilities.isEventDispatchThread()) {
            try {
                SwingUtilities.invokeAndWait(this::repaintYou);
            } catch (InvocationTargetException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            this.repaintYou();
        }
    }

    void repaintYou() {
        this.setText(Integer.toString(number.getNumber()));
        switch (number.getNumber()) {
            case 0:
                this.setBackground(Color.red);
                break;
            case 1:
                this.setBackground(Color.green);
                break;
            case 2:
                this.setBackground(Color.blue);
                break;
            case 3:
                this.setBackground(Color.black);
                break;
            case 4:
                this.setBackground(Color.cyan);
                break;
            default:
                this.setBackground(Color.yellow);
                break;
        }
    }

}

class ClickListener implements ActionListener {

    private Number number;

    public ClickListener(Number number) {
        this.number = number;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Thread th = new Thread(() -> {
            while (true) {
                number.setNumber(ThreadLocalRandom.current().nextInt(6));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                }
            }
        });
        th.setDaemon(true);
        th.start();
    }

}
