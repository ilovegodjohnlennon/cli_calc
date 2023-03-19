import java.util.function.BinaryOperator;

// mainly for ops like '+', '-', '*', '/', etc.
public class BinaryOperatorNode extends ExpressionNode{
    private BinaryOperator<Double> operator;
    private ExpressionNode arg1;
    private ExpressionNode arg2;

    @Override
    public double evaluate(){
        return operator.apply(arg1.evaluate(), arg2.evaluate());
    }

    public BinaryOperatorNode(InfixOperatorToken token, ExpressionNode arg1_, ExpressionNode arg2_)
    {
        super(token.getName());
        operator = token.getOperator();
        arg1 = arg1_;
        arg2 = arg2_;
    }

    @Override
    public void print(int depth){
        for(int i = 0; i < depth; i++) System.out.print(' ');
        System.out.println("BINARY_OPERATOR: " + getName());

        arg1.print(depth + 4);
        arg2.print(depth + 4);
    }
}
