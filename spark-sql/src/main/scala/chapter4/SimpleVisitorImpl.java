package chapter4;

public class SimpleVisitorImpl implements Visitor{

    @Override
    public void visit(Beijing beijing) {
        System.out.println("Hello beijing!");
    }

    @Override
    public void visit(Shanghai shanghai) {
        System.out.println("Hello shanghai!");
    }
}
