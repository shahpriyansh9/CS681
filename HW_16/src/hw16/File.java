package hw16;

import java.time.LocalDateTime;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class File extends FSElement {
    private final Lock lock = new ReentrantLock();

    public File(Directory parent, String name, int size, LocalDateTime creationTime) {
        super(parent, name, size, creationTime);
        parent.appendChild(this);
    }

    public boolean isDirectory() {
        return false;
    }

    public boolean isFile() {
        return true;
    }

    public boolean isLink() {
        return false;
    }

    public void accept(FSVisitor v) {
        lock.lock();
        try {
            v.visit(this);
        } finally {
            lock.unlock();
        }
    }
}
