package de.holube.ex.ex09;

import java.util.List;

public class StringContains {

    public static void main(String[] args) {
        final String[] strings = {"hallo", null, "", "welt", "geradeaus",
                "jahr", "achtung"};
        final char ch = 'a';
        List<String> result = contains(strings, ch);
        System.out.println(result);
    }
    
}
