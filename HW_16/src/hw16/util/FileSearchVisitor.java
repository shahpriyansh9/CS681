package hw16.util;

import java.util.concurrent.ConcurrentLinkedQueue;
import hw16.FSVisitor;
import hw16.Directory;
import hw16.File;
import hw16.Link;

public class FileSearchVisitor implements FSVisitor {
    private String fileName;
    private ConcurrentLinkedQueue<File> foundFiles;

    public FileSearchVisitor(String fileName) {
        foundFiles = new ConcurrentLinkedQueue<>();
        this.fileName = fileName;
    }

    public void visit(Link link) {
        
    }

    public void visit(Directory dir) {
        
    }

    public void visit(File file) {
       if (file.getName().equals(fileName)) {
            foundFiles.add(file);
       }
    }

    public ConcurrentLinkedQueue<File> getFoundFiles() {
        return new ConcurrentLinkedQueue<>(foundFiles); // Return a copy of the files
    }
}
