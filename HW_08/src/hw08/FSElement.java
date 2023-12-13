package hw08;

import java.time.LocalDateTime;

public abstract class FSElement
{
    protected Directory parent;
    protected String name;
    protected int size;
    protected LocalDateTime creationTime;

    public FSElement(Directory parent, String name, int size, LocalDateTime creationTime)
    {
        this.parent = parent;
        this.name = name;
        this.size = size;
        this.creationTime = creationTime;

    }
    
    public void setParent(Directory parent)
    {
        this.parent = parent;
    }
    public Directory getParent()
    {
        return parent;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return name;
    }

    public int getSize()
    {
        return size;
    }

    public LocalDateTime getCreationTime()
    {
        return creationTime;
    }

    public abstract boolean isDirectory();
    public abstract boolean isFile();
    public abstract boolean isLink();


} 