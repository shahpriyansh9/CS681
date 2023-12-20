package hw11;


import hw11.util.FileCrawlingVisitor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class FileSystem {
    protected String name;
    protected int capacity;
    protected int id;
    private List<FSElement> rootdirs = Collections.synchronizedList(new LinkedList<>());
    private ExecutorService threadPool;
    private AtomicBoolean crawlingTerminated = new AtomicBoolean(false);
    private ConcurrentLinkedQueue<File> sharedFileList = new ConcurrentLinkedQueue<>();

    public FSElement init(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        
        for (int i = 0; i < 3; i++) {
            FSElement root = createDefaultRoot();
            if (root.isDirectory() && capacity >= root.getSize()) {
                setRoot(root);
            }
        }
        this.id = rootdirs.hashCode();
        return rootdirs.isEmpty() ? null : rootdirs.get(0);
    }

    protected FSElement createDefaultRoot() {
        Directory root = new Directory(null, "root", 0, LocalDateTime.now());
        for (int i = 0; i < 1; i++) { // Create only one file per root
            File file = new File(root, "testFile" + i + ".txt", 100, LocalDateTime.now());
            root.appendChild(file);
        }
        return root;
    }   

    protected void setRoot(FSElement root) {
        rootdirs.add(root);
    }

    public void startCrawling() {
        threadPool = Executors.newFixedThreadPool(rootdirs.size());
        for (FSElement root : rootdirs) {
            threadPool.execute(() -> {
                ThreadLocal<FileCrawlingVisitor> threadLocalVisitor = ThreadLocal.withInitial(FileCrawlingVisitor::new);
    
                while (!crawlingTerminated.get()) {
                    root.accept(threadLocalVisitor.get());
                    List<File> files = threadLocalVisitor.get().getFiles();
                    synchronized (sharedFileList) {
                        for (File file : files) {
                            if (!sharedFileList.contains(file)) {
                                sharedFileList.add(file);
                            }
                        }
                    }
                    threadLocalVisitor.get().clearFiles();
                }
                System.out.println("Crawling completed for thread: " + Thread.currentThread().getName());
            });
        }
    }
    public List<File> getCollectedFiles() {
        return new ArrayList<>(sharedFileList);
    }

    public void stopCrawling() {
        crawlingTerminated.set(true);
        System.out.println("Sending termination signal to all crawling threads...");
        threadPool.shutdown(); // Initiates an orderly shutdown
    
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow(); // Forcefully shuts down if not terminated
                if (!threadPool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Thread pool did not terminate.");
            }
        } catch (InterruptedException ie) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    
        System.out.println("All crawling threads have been terminated.");
    }

    public void printCollectedFiles() {
        int fileCount = 0;
        for (File file : sharedFileList) {
            System.out.println("File: " + file.getName());
            fileCount++;
            if (fileCount > 1000) { // Limit to printing only the first 1000 files
                break;
            }
        }
        System.out.println("Total number of files: " + sharedFileList.size());
    }

    public List<FSElement> getRootDirs() {
        return rootdirs;
    }

    public int getCapacity() {
        return capacity;
    }

    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem() {
            @Override
            protected FSElement createDefaultRoot() {
                Directory root = new Directory(null, "root", 0, LocalDateTime.now());
                File file = new File(root, "testFile.txt", 100, LocalDateTime.now());
                root.appendChild(file); // Add the file to the root directory
                return root;
            }
        };

        fileSystem.init("TestFileSystem", 1000);
        System.out.println("Starting file system crawling...");
        fileSystem.startCrawling();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted: " + e.getMessage());
        }

        System.out.println("Stopping file system crawling...");
        fileSystem.stopCrawling();
        System.out.println("Collected files: " + fileSystem.getCollectedFiles());
    }
}
