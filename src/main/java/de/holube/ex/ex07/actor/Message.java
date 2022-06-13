package de.holube.ex.ex07.actor;

public class Message {

    private String type;
    private Object[] data;
    private Thread sender;

    public Message(Thread sender, String type, Object... data) {
        this.sender = sender;
        this.type = type;
        this.data = data;
    }

    public Message(String type, Object... data) {
        this(Thread.currentThread(), type, data);
    }

    public String getType() {
        return type;
    }

    public Object[] getData() {
        return data;
    }

    public Thread getSender() {
        return sender;
    }

}
