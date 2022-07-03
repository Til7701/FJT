package de.holube.ex.ex09;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CharacterCount {

    public static void main(String[] args) {
        final String[] strings = {"hallo", null, "", "welt", "geradeaus",
                "jahr", "achtung", "a", "ax", "xa"};
        final char ch = 'a';
        int result = numberOf(strings, ch);
        System.out.println(result);
    }

    public static int numberOf(String[] strings, char ch) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        List<Future<Integer>> futures = new ArrayList<>();

        for (String s : strings) {
            futures.add(executorService.submit(new SearchTask(s, ch)));
        }
        executorService.shutdown();

        int result = 0;
        boolean interrupted = Thread.interrupted();
        for (Future<Integer> future : futures) {
            boolean gotResult = false;
            while (!gotResult) {
                try {
                    result += future.get();
                    gotResult = true;
                } catch (InterruptedException e) {
                    interrupted = true;
                } catch (ExecutionException e) {
                    gotResult = true;
                }
            }
        }
        if (interrupted)
            Thread.currentThread().interrupt();
        return result;
    }

    private record SearchTask(String string, char ch) implements Callable<Integer> {

        @Override
        public Integer call() {
            if (string == null || string.isEmpty())
                return 0;

            int counter = 0;
            for (int i = 0; i < string.length(); i++) {
                if (string.charAt(i) == ch)
                    counter++;
            }
            return counter;
        }

    }

}
