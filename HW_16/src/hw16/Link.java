package hw16;

import java.time.LocalDateTime;

public class Link extends FSElement {
    private FSElement target;

    public Link(Directory parent, String name, int size, LocalDateTime creDateTime, FSElement target) {
        super(parent, name, size, creDateTime);
        this.target = target;
    }

    public boolean isDirectory() {
        return false;
    }

    public boolean isFile() {
        return false;
    }

    public boolean isLink() {
        return true;
    }

    public void setTarget(FSElement target) {
        this.target = target;
    }

    public FSElement getTarget() {
        return target;
    }

    public void accept(FSVisitor v) {
        v.visit(this);
    }
}
