package de.holube.ex.ex07.actor;

public class FindMasterActor extends Actor {

    public Response onReceive(Message msg) {
        FindActor left = new FindActor();
        FindActor right = new FindActor();
        String str = (String) msg.getData()[0];
        char ch = (char) msg.getData()[1];
        long handleLeft = left.send(new Message("find", str.substring(0, str.length() / 2), ch));
        long handleRight = right.send(new Message("find", str.substring(str.length() / 2), ch));
        Response leftResponse = left.getResponse(handleLeft);
        Response rightResponse = right.getResponse(handleRight);
        int numberL = (Integer) leftResponse.getData()[0];
        int numberR = (Integer) rightResponse.getData()[0];
        return new Response(msg, numberL + numberR);
    }

}
