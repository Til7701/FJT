package de.holube.ex.ex07.actor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Actor extends Thread {

    private final BlockingQueue<ActorMessage> messageQueue = new ArrayBlockingQueue<>(10);
    private final ConcurrentHashMap<Long, Response> responseMap = new ConcurrentHashMap<>();
    private long messageHandle = 0;

    protected Actor() {
        setDaemon(true);
        start();
    }

    @Override
    public final void run() {
        ActorMessage msg;
        Response response;
        while (true) {
            // warten auf Nachrichten
            try {
                msg = messageQueue.take();
            } catch (InterruptedException e) {
                continue;
            }

            // verarbeiten einer Nachricht
            response = onReceive(msg.message);

            // speichern des Ergebnisses
            synchronized (responseMap) {
                responseMap.put(msg.handle, response);
                responseMap.notifyAll();
            }
        }
    }

    // asynchrones Senden einer Nachricht; geliefert wird ein eindeutiges "Handle"
    // ueber das spaeter die Antwort abgefragt werden kann
    public long send(Message msg) {
        try {
            messageQueue.put(new ActorMessage(messageHandle, msg));
        } catch (InterruptedException e) {
            // ignore
        }
        return messageHandle++;
    }

    // abfragen einer Antwort zu einer nachricht ueber das entsprechende Handle der
    // Methode send;
    // es kann vorausgesetzt werden, dass das Handle ein gueltiges Handle ist,
    // das vorher via send erzeugt wurde und dessen Antwort noch nicht abgerufen
    // worden ist
    public Response getResponse(long msgHandle) {
        synchronized (responseMap) {
            while (true) {
                while (responseMap.isEmpty()) {
                    try {
                        responseMap.wait();
                    } catch (InterruptedException e) {
                    }
                }
                Response response = responseMap.remove(msgHandle);
                if (response != null) {
                    return response;
                }
            }
        }
    }

    public abstract Response onReceive(Message msg);

    private class ActorMessage {
        long handle;
        Message message;

        public ActorMessage(long handle, Message message) {
            super();
            this.handle = handle;
            this.message = message;
        }
    }

}
