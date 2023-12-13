package hw10;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class Directory extends FSElement {
    private final LinkedList<FSElement> children = new LinkedList<>();
    private final ReentrantLock lock = new ReentrantLock(); // Lock for thread safety

    public Directory(Directory parent, String name, int size, LocalDateTime creationTime) {
        super(parent, name, size, creationTime);
    }

    public boolean isDirectory() {
        return true;
    }

    public boolean isFile() {
        return false;
    }

    public boolean isLink() {
        return false;
    }

    public void appendChild(FSElement child) {
        lock.lock();
        try {
            this.children.add(child);
            child.setParent(this);
        } finally {
            lock.unlock();
        }
    }

    public LinkedList<FSElement> getChildren() {
        lock.lock();
        try {
            return new LinkedList<>(this.children); 
        } finally {
            lock.unlock();
        }
    }

    public int countChildren() {
        lock.lock();
        try {
            return this.children.size();
        } finally {
            lock.unlock();
        }
    }

    public int getTotalSize() {
        lock.lock();
        try {
            int totalSize = 0;
            for (FSElement fsElement : children) {
                if (fsElement.isDirectory()) {
                    totalSize += ((Directory) fsElement).getTotalSize();
                } else {
                    totalSize += fsElement.getSize();
                }
            }
            return totalSize;
        } finally {
            lock.unlock();
        }
    }

    public LinkedList<Directory> getSubDirectories() {
        lock.lock();
        try {
            LinkedList<Directory> subDirectory = new LinkedList<>();
            for (FSElement fsElement : children) {
                if (fsElement.isDirectory()) {
                    subDirectory.add((Directory) fsElement);
                }
            }
            return subDirectory;
        } finally {
            lock.unlock();
        }
    }

    public LinkedList<File> getFiles() {
        lock.lock();
        try {
            LinkedList<File> files = new LinkedList<>();
            for (FSElement fsElement : children) {
                if (fsElement.isFile()) {
                    files.add((File) fsElement);
                }
            }
            return files;
        } finally {
            lock.unlock();
        }
    }
}
