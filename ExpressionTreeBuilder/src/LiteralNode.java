public class LiteralNode extends ExpressionNode{
    private double value;

    @Override
    public double evaluate() {
        return value;
    }

    public LiteralNode(LiteralToken token){
        super("");
        value = token.getValue();
    }

    @Override
    public void print(int depth){
        for(int i = 0; i < depth; i++) System.out.print(' ');
        System.out.println("LITERAL: " + value);
    }
}
