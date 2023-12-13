package hw11.util;

import java.util.LinkedList;
import java.util.List;

import hw11.Directory;
import hw11.FSVisitor;
import hw11.File;
import hw11.Link;

public class FileCrawlingVisitor implements FSVisitor {
    private List<File> files = new LinkedList<>();

    public void visit(Link link) {
        // Implementation if needed
    }

    public void visit(Directory dir) {
        // Implementation if needed
    }

    public void visit(File file) {
        files.add(file);
    }

    public List<File> getFiles() {
        return new LinkedList<>(files); // Return a copy of the files
    }

    public void clearFiles() {
        files.clear(); // Clears the internal list of files
    }
}
