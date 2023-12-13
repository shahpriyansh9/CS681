package hw08;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class Directory extends FSElement
{
    private final LinkedList<FSElement> children = new LinkedList<>();
        public Directory(Directory parent, String name, int size, LocalDateTime creationTime)
        {
            super(parent, name, size, creationTime);
        }
    public boolean isDirectory()
    {
        return true;
    }
    
    public boolean isFile() 
    {
               return false;
    }
    
    public boolean isLink() 
    {
                return false;
    }

   

    public void appendChild(FSElement child)
    {
        this.children.add(child);
        child.setParent(this);
    }

    
    public LinkedList<FSElement> getChildren()
    {
        return this.children;
    }

    public int countChildren()
    {
        return this.children.size();
    }

    public int getTotalSize()
    {
        int totalsize = 0;
        for(FSElement fsElement : children)
        {
            if(fsElement.isDirectory())
            {
                totalsize += ((Directory)fsElement).getTotalSize();
            }
            else
            {
                totalsize += fsElement.getSize();
            }
        }
        return totalsize;
    }

       
    public LinkedList<Directory> getSubDirectories()
    {
        LinkedList<Directory> SubDirectory = new LinkedList<>();

        for(FSElement fsElement : children)
        {
            if(fsElement.isDirectory())
            {
                SubDirectory.add((Directory) fsElement);
            }
        }
        return SubDirectory;
    }

    public LinkedList<File> getFiles()
    {
        LinkedList<File> files = new LinkedList<>();

        for(FSElement fsElement : children)
        {
            if(!fsElement.isDirectory())
            {
                files.add((File)fsElement);
            }
        }
        return files;
    }


    
}
