package hw08;

import java.time.LocalDateTime;
 

public class Link extends FSElement
{
    private FSElement target;

    public Link(Directory parent, String name, int size, LocalDateTime creDateTime, FSElement target)
    {
        super(parent, name, size, creDateTime);
        this.target = target;
    }

    
    public boolean isDirectory() {
        // TODO Auto-generated method stub
        return false;
    }

    
    public boolean isFile() {
        // TODO Auto-generated method stub
        return false;
    }

    
    public boolean isLink() {
        // TODO Auto-generated method stub
        return true;
    }

    public void setTarget(FSElement target)
    {
        this.target = target;
    }

    public FSElement getTarget()
    {
        return target;
    }
}
