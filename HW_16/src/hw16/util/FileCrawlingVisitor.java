package hw16.util;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.ArrayList;
import java.util.List;

import hw16.Directory;
import hw16.FSVisitor;
import hw16.File;
import hw16.Link;

public class FileCrawlingVisitor implements FSVisitor {
    private List<File> files = new CopyOnWriteArrayList<>();

    public void visit(Link link) {
        
    }

    public void visit(Directory dir) {
       
    }

    public void visit(File file) {
        files.add(file);
    }

    public List<File> getFiles() {
        return new ArrayList<>(files); 
    }

    public void clearFiles() {
        files.clear(); 
    }
}
