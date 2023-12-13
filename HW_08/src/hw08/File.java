package hw08;

import java.time.LocalDateTime;

public class File extends FSElement 
{
    public File(Directory parent, String name, int size, LocalDateTime creationTime)
    {
        super(parent, name, size, creationTime);
        parent.appendChild(this);
    }

   
    public boolean isDirectory() {
        // TODO Auto-generated method stub
        return false;
    }

    
    public boolean isFile() {
        // TODO Auto-generated method stub
        return true;
    }

    
    public boolean isLink() {
        // TODO Auto-generated method stub
        return false;
    }
    
}
