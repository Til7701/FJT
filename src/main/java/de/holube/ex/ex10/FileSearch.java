package de.holube.ex.ex10;

import java.io.File;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicReference;

public class FileSearch {

    public static String findPath(String path, String fileName) {
        FileSearchTask task = new FileSearchTask(null, path, fileName, new AtomicReference<>());
        return ForkJoinPool.commonPool().invoke(task);
    }

    private static class FileSearchTask extends CountedCompleter<String> {

        private final String path;
        private final String fileName;

        private final AtomicReference<String> result;

        public FileSearchTask(FileSearchTask parent, String path, String fileName, AtomicReference<String> result) {
            super(parent);
            this.path = path;
            this.fileName = fileName;
            this.result = result;
        }

        @Override
        public String getRawResult() {
            return result.get();
        }

        @Override
        public void compute() {
            File file = new File(path);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (f.isDirectory()) {
                            addToPendingCount(1);
                            FileSearchTask task = new FileSearchTask(this, f.getAbsolutePath(), fileName, result);
                            task.fork();
                        } else if (f.getName().equals(fileName)) {
                            result.set(f.getAbsolutePath());
                        }
                    }
                }
            } else if (file.getName().equals(fileName)) {
                result.set(file.getAbsolutePath());
            }
            propagateCompletion();
        }
    }

    public static void main(String[] args) {
        String path = "C:\\Users\\Tilman\\IdeaProjects\\FJT";
        String fileName = "FileSearch.java";
        String result = findPath(path, fileName);
        System.out.println(result);
    }

}
