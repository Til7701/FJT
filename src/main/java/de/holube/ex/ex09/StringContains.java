package de.holube.ex.ex09;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class StringContains {

    public static void main(String[] args) {
        final String[] strings = {"hallo", null, "", "welt", "geradeaus",
                "jahr", "achtung", "a", "ax", "xa"};
        final char ch = 'a';
        List<String> result = contains(strings, ch);
        System.out.println(result);
    }

    public static List<String> contains(String[] strings, char ch) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        ExecutorCompletionService<String> completionService = new ExecutorCompletionService<>(executorService);

        for (String s : strings) {
            completionService.submit(new SearchTask(s, ch));
        }
        executorService.shutdown();

        List<String> result = new ArrayList<>();
        boolean interrupted = Thread.interrupted();
        for (int i = 0; i < strings.length; i++) {
            boolean gotResult = false;
            while (!gotResult) {
                try {
                    Future<String> future = completionService.take();
                    String string = future.get();
                    if (string != null) result.add(string);
                    gotResult = true;
                } catch (InterruptedException e) {
                    interrupted = true;
                } catch (ExecutionException e) {
                    // ignore
                }
            }
        }
        if (interrupted) Thread.currentThread().interrupt();
        return result;
    }

    private record SearchTask(String string, char ch) implements Callable<String> {

        @Override
        public String call() {
            if (string == null || string.isEmpty()) return null;

            String c = "" + ch;
            if (string.contains(c)) return string;
            return null;
        }

    }

}
