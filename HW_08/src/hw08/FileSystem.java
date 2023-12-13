package hw08;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class FileSystem {
    private static FileSystem instance = null;
    private LinkedList<Directory> rootDirs;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final ReentrantLock rootDirsLock = new ReentrantLock();

    private FileSystem() {
        this.rootDirs = new LinkedList<>();
    }

    public static FileSystem getFileSystem() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new FileSystem();
            }
            return instance;
        } finally {
            lock.unlock();
        }
    }

    public void appendRootDir(Directory root) {
        rootDirsLock.lock();
        try {
            rootDirs.add(root);
        } finally {
            rootDirsLock.unlock();
        }
    }

    public LinkedList<Directory> getRootDirs() {
        rootDirsLock.lock();
        try {
            return new LinkedList<>(rootDirs); // Return a copy to avoid concurrent modification issues
        } finally {
            rootDirsLock.unlock();
        }
    }

    public static void main(String[] args) {
        Runnable task = () -> {
            FileSystem fileSystem = FileSystem.getFileSystem();
            Directory root = new Directory(null, "root", 0, LocalDateTime.now());
            fileSystem.appendRootDir(root);
            System.out.println("FileSystem instance: " + fileSystem + ", Root Directories: " + fileSystem.getRootDirs());
        };

        Thread[] threads = new Thread[3];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(task);
            threads[i].start();
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
