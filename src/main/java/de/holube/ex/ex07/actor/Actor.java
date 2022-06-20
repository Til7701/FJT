package de.holube.ex.ex07.actor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Actor extends Thread {

    private final BlockingQueue<ActorMessage> messageQueue = new ArrayBlockingQueue<>(10);
    private final ConcurrentHashMap<Long, BlockingQueue<Response>> responseMap = new ConcurrentHashMap<>();
    private final AtomicLong messageHandle = new AtomicLong(0);

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
            responseMap.get(msg.handle).add(response);
        }
    }

    // asynchrones Senden einer Nachricht; geliefert wird ein eindeutiges "Handle"
    // ueber das spaeter die Antwort abgefragt werden kann
    public long send(Message msg) {
        long mh = messageHandle.getAndIncrement();
        BlockingDeque<Response> responseBlockingDeque = new LinkedBlockingDeque<>();
        responseMap.put(mh, responseBlockingDeque);
        //noinspection ResultOfMethodCallIgnored
        messageQueue.offer(new ActorMessage(mh, msg));
        return mh;
    }

    // abfragen einer Antwort zu einer nachricht ueber das entsprechende Handle der
    // Methode send;
    // es kann vorausgesetzt werden, dass das Handle ein gueltiges Handle ist,
    // das vorher via send erzeugt wurde und dessen Antwort noch nicht abgerufen
    // worden ist
    public Response getResponse(long msgHandle) {
        Response response = null;
        BlockingQueue<Response> responseBlockingDeque = responseMap.get(msgHandle);
        while (response == null) {
            try {
                response = responseBlockingDeque.take();
            } catch (InterruptedException e) {
                // ignore
            }
        }
        responseMap.remove(msgHandle);
        return response;
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
