import java.util.function.UnaryOperator;

// for prefix '-', '+' and functions like sin(), cos(), ln(), ...
public class UnaryOperatorNode extends ExpressionNode{
    private UnaryOperator<Double> operator;
    private ExpressionNode arg;

    @Override
    public double evaluate(){
        return operator.apply(arg.evaluate());
    }
}
