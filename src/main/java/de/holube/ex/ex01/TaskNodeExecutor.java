// vergleiche Oechsle, Seite 59ff
package de.holube.ex.ex01;

import java.util.ArrayList;
import java.util.List;

interface Task<D, R> {
    public boolean isDivisible();

    public R execute();

    public List<Task<D, R>> split();

    public R combine(List<R> results);
}

class TaskNodeExecutor<D, R> extends Thread {
    private Task<D, R> task;
    private R result;

    public TaskNodeExecutor(Task<D, R> task) {
        this.task = task;
    }

    @Override
    public void run() {
        if (task.isDivisible()) {
            List<Task<D, R>> subtasks = task.split();
            List<TaskNodeExecutor<D, R>> threads = new ArrayList<>();
            for (int i = 0; i < subtasks.size() - 1; i++) {
                TaskNodeExecutor<D, R> thread = new TaskNodeExecutor<>(subtasks.get(i));
                threads.add(thread);
                thread.start();
            }
            TaskNodeExecutor<D, R> thisThread = new TaskNodeExecutor<>(subtasks.get(subtasks.size() - 1));
            List<R> subresults = new ArrayList<>();
            thisThread.run();
            subresults.add(thisThread.getResult());
            for (TaskNodeExecutor<D, R> thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    return;
                }
                subresults.add(thread.getResult());
            }
            result = task.combine(subresults);
        } else { // !task.isDivisible()
            result = task.execute();
        }
    }

    private R getResult() {
        return result;
    }

    // stoesst den parallelisierten Teile-und-Herrsche-Algorithmus an
    public static <T, R> R executeAll(Task<T, R> task) {
        TaskNodeExecutor<T, R> root = new TaskNodeExecutor<>(task);
        root.run(); // ok, kein extra Thread notwendig
        return root.getResult();
    }
}
