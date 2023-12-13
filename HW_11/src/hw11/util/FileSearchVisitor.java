package hw11.util;

import java.util.LinkedList;

import hw11.FSVisitor;
import hw11.Directory;
import hw11.File;
import hw11.Link;



public class FileSearchVisitor implements FSVisitor
{
    private String fileName;
    private LinkedList<File> foundFiles;

    public FileSearchVisitor(String fileName)
    {
        foundFiles = new LinkedList<>();
        this.fileName = fileName;
    }

    
    public void visit(Link link) 
    {
        return;
    }

   
    public void visit(Directory dir) 
    {
       return;
    }

    
    public void visit(File file) 
    {
       if(file.getName().equals(fileName))
       {
            foundFiles.add(file);
       }
    }

    public LinkedList<File> getFoundFiles()
    {
        return foundFiles;
    }
    
}
