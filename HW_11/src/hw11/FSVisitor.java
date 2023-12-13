package hw11;

public interface FSVisitor
{
     public abstract void visit(Link link);
     public abstract void visit(Directory dir);
     public abstract void visit(File file);

}