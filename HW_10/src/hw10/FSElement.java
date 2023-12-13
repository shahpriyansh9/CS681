package hw10;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

public abstract class FSElement {
    private Directory parent;
    private String name;
    private final int size; // Make final if size is not supposed to change
    private final LocalDateTime creationTime;
    private final ReentrantLock lock;

    public FSElement(Directory parent, String name, int size, LocalDateTime creationTime) {
        this.parent = parent;
        this.name = name;
        this.size = size;
        this.creationTime = creationTime;
        this.lock = new ReentrantLock();
    }

    public void setParent(Directory parent) {
        lock.lock();
        try {
            this.parent = parent;
        } finally {
            lock.unlock();
        }
    }

    public Directory getParent() {
        lock.lock();
        try {
            return parent;
        } finally {
            lock.unlock();
        }
    }

    public void setName(String name) {
        lock.lock();
        try {
            this.name = name;
        } finally {
            lock.unlock();
        }
    }

    public String getName() {
        lock.lock();
        try {
            return name;
        } finally {
            lock.unlock();
        }
    }

    public int getSize() {
        lock.lock();
        try {
            return size;
        } finally {
            lock.unlock();
        }
    }

        public LocalDateTime getCreationTime() {
        lock.lock();
        try {
            return creationTime;
        } finally {
            lock.unlock();
        }
    }

    public abstract boolean isDirectory();
    public abstract boolean isFile();
    public abstract boolean isLink();
}
