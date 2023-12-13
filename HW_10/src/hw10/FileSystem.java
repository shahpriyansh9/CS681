package hw10;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class FileSystem {
    private static final AtomicReference<FileSystem> instance = new AtomicReference<>();
    private LinkedList<Directory> rootDirs;
    private static final AtomicBoolean keepRunning = new AtomicBoolean(true);

    private FileSystem() {
        this.rootDirs = new LinkedList<>();
    }

    public static FileSystem getFileSystem() {
        if (instance.get() == null) {
            instance.compareAndSet(null, new FileSystem());
        }
        return instance.get();
    }

    public void appendRootDir(Directory root) {
        synchronized (this.rootDirs) {
            if (keepRunning.get()) {
                rootDirs.add(root);
            }
        }
    }

    public LinkedList<Directory> getRootDirs() {
        synchronized (this.rootDirs) {
            return new LinkedList<>(rootDirs); // Return a copy to avoid concurrent modification issues
        }
    }

    public static void main(String[] args) {
        Runnable task = () -> {
            while (keepRunning.get()) {
                FileSystem fileSystem = FileSystem.getFileSystem();
                Directory root = new Directory(null, "root", 0, LocalDateTime.now());
                fileSystem.appendRootDir(root);
                System.out.println("FileSystem instance: " + fileSystem + ", Root Directories: " + fileSystem.getRootDirs());
            }
        };

        Thread[] threads = new Thread[12]; // More than 10 threads
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(task);
            threads[i].start();
        }

        // Sleep for some time then set keepRunning to false for 2-step termination
        try {
            Thread.sleep(10000); // Let threads run for 10 seconds
            keepRunning.set(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
