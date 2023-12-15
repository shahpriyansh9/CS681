package hw16;

import java.time.LocalDateTime;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class FSElement {
    protected Directory parent;
    protected String name;
    protected int size;
    protected LocalDateTime creationTime;
    private final Lock lock = new ReentrantLock();

    public FSElement(Directory parent, String name, int size, LocalDateTime creationTime) {
        this.parent = parent;
        this.name = name;
        this.size = size;
        this.creationTime = creationTime;
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
        return parent;
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
        return name;
    }

    public int getSize() {
        return size;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public abstract boolean isDirectory();
    public abstract boolean isFile();
    public abstract boolean isLink();
    public abstract void accept(FSVisitor v);
}
