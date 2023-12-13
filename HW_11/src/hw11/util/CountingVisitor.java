package hw11.util;

import hw11.FSVisitor;
import hw11.Directory;
import hw11.File;
import hw11.Link;

public class CountingVisitor implements FSVisitor
{
    
    


    private int dirNum = 0;
    private int fileNum = 0;
    private int linkNum = 0;

    
    public void visit(Link link) 
    {
        linkNum++;
        
    }

    
    public void visit(Directory dir) 
    {
        dirNum++;
        
    }

    
    public void visit(File file) 
    {
        fileNum++;
        
    }

   
    
}
