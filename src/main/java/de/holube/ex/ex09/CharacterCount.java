package de.holube.ex.ex09;

public class CharacterCount {

    public static void main(String[] args) {
        final String[] strings = {"hallo", null, "", "welt", "geradeaus",
                "jahr", "achtung"};
        final char ch = 'a';
        int result = numberOf(strings, ch);
        System.out.println(result);
    }

}
