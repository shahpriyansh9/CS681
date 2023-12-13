package hw10;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

public class Link extends FSElement {
    private FSElement target;
    private final ReentrantLock lock; 

    public Link(Directory parent, String name, int size, LocalDateTime creDateTime, FSElement target) {
        super(parent, name, size, creDateTime);
        this.target = target;
        this.lock = new ReentrantLock();
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public boolean isFile() {
        return false;
    }

    @Override
    public boolean isLink() {
        return true;
    }

    public void setTarget(FSElement target) {
        lock.lock();
        try {
            this.target = target;
        } finally {
            lock.unlock();
        }
    }

    public FSElement getTarget() {
        lock.lock();
        try {
            return target;
        } finally {
            lock.unlock();
        }
    }
}
