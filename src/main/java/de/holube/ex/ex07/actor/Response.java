package de.holube.ex.ex07.actor;

public class Response extends Message {

    private Message message; // response to that message

    public Response(Message msg, Actor sender, String type, Object... data) {
        super(sender, type, data);
        this.message = msg;
    }

    public Response(Message msg, String type, Object... data) {
        this(msg, (Actor) Thread.currentThread(), type, data);
    }

    public Response(Message msg, Object... data) {
        this(msg, (Actor) Thread.currentThread(), null, data);
    }

    public Message getMessage() {
        return message;
    }

}
