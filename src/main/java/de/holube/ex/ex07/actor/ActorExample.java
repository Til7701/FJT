package de.holube.ex.ex07.actor;

public class ActorExample {

    public static void main(String[] args) {
        final String str = "0283237823286322232323237";
        final char ch = '2';
        FindMasterActor actor = new FindMasterActor();
        long handle = actor.send(new Message("find", str, ch));
        System.out.println(actor.getResponse(handle).getData()[0]); // 10
    }

}
