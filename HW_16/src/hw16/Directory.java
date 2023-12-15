package hw16;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class Directory extends FSElement {
    private final ConcurrentLinkedQueue<FSElement> children = new ConcurrentLinkedQueue<>();

    public Directory(Directory parent, String name, int size, LocalDateTime creationTime) {
        super(parent, name, size, creationTime);
    }

    // Override methods
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
        this.children.add(child);
        child.setParent(this);
    }

    public ConcurrentLinkedQueue<FSElement> getChildren() {
        return new ConcurrentLinkedQueue<>(this.children);
    }

    public int countChildren() {
        return this.children.size();
    }

    public int getTotalSize() {
        int totalSize = 0;
        for (FSElement fsElement : children) {
            if (fsElement.isDirectory()) {
                totalSize += ((Directory) fsElement).getTotalSize();
            } else {
                totalSize += fsElement.getSize();
            }
        }
        return totalSize;
    }

    public ConcurrentLinkedQueue<Directory> getSubDirectories() {
        return children.stream()
                       .filter(FSElement::isDirectory)
                       .map(fsElement -> (Directory) fsElement)
                       .collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
    }

    public ConcurrentLinkedQueue<File> getFiles() {
        return children.stream()
                       .filter(FSElement::isFile)
                       .map(fsElement -> (File) fsElement)
                       .collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
    }

    public void accept(FSVisitor v) {
        v.visit(this);
        for (FSElement e : children) {
            e.accept(v);
        }
    }
}
