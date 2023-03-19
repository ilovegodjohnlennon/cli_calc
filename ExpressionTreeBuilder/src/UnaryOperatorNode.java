import java.util.function.UnaryOperator;

// for prefix '-', '+' and functions like sin(), cos(), ln(), ...
public class UnaryOperatorNode extends ExpressionNode{
    private UnaryOperator<Double> operator;
    private ExpressionNode arg;

    public UnaryOperatorNode(PrefixOperatorToken token, ExpressionNode arg_){
        super(token.getName());
        operator = token.getOperator();
        arg = arg_;
    }

    @Override
    public double evaluate(){
        return operator.apply(arg.evaluate());
    }

    @Override
    public void print(int depth){
        for(int i = 0; i < depth; i++) System.out.print(' ');
        System.out.println("UNARY_OPERATOR: " + getName());

        arg.print(depth + 4);
    }
}
