package de.holube.ex.ex11;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {

    public static void main(String[] args) {
        // nur zum Testen, kann ignoriert werden
        List<TestTask> tasks = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger();

        for (int i = 0; i < 20; i++) {
            tasks.add(new TestTask(true, counter));
        }

        ForkJoinTask.invokeAll(tasks);

        System.out.println("Done " + counter.get());
    }

    private static class TestTask extends ForkJoinTask<Long> {

        private final boolean invokeMore;
        private final AtomicInteger counter;

        public TestTask(boolean invokeMore, AtomicInteger counter) {
            this.invokeMore = invokeMore;
            this.counter = counter;
        }

        @Override
        public Long getRawResult() {
            return null;
        }

        @Override
        protected void setRawResult(Long value) {

        }

        @Override
        protected boolean exec() {
            System.out.println("in exec " + counter.getAndIncrement());

            if (!invokeMore) {
                return true;
            }

            List<TestTask> tasks = new ArrayList<>();

            for (int i = 0; i < 20; i++) {
                tasks.add(new TestTask(false, counter));
            }

            ForkJoinTask.invokeAll(tasks);
            return true;
        }
    }

}
