package hw16.util;

import hw16.FSVisitor;
import hw16.Directory;
import hw16.File;
import hw16.Link;

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
