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

    public BinaryOperatorNode(BinaryOperator<Double> operator_, ExpressionNode arg1_, ExpressionNode arg2_)
    {
        operator = operator_;
        arg1 = arg1_;
        arg2 = arg2_;
    }
}
