package chapter4;

public class Beijing implements City{
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
