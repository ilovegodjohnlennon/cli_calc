public class LiteralNode extends ExpressionNode{
    private double value;

    @Override
    public double evaluate() {
        return value;
    }
}
