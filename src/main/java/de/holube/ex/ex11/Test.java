package de.holube.ex.ex11;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;

public class Test {

    public static void main(String[] args) {
        // nur zum Testen, kann ignoriert werden
        List<TestTask> tasks = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            tasks.add(new TestTask(true));
        }

        ForkJoinTask.invokeAll(tasks);

        System.out.println("Done");
    }

    private static class TestTask extends ForkJoinTask<Long> {

        private final boolean invokeMore;

        public TestTask(boolean invokeMore) {
            this.invokeMore = invokeMore;
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
            if (!invokeMore)
                return true;

            System.out.println("in exec");
            List<TestTask> tasks = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                tasks.add(new TestTask(false));
            }

            ForkJoinTask.invokeAll(tasks);
            return true;
        }
    }

}
