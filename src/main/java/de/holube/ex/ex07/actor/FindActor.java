package de.holube.ex.ex07.actor;

public class FindActor extends Actor {

    public Response onReceive(Message msg) {
        String str = (String) msg.getData()[0];
        char ch = (char) msg.getData()[1];
        int number = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) {
                number++;
            }
        }
        return new Response(msg, number);
    }

}
