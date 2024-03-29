import java.util.function.BinaryOperator;

// INFIX OPERATORS HAVE PRIORITY!!!!
// +, -: 10
// *, /: 20
public class InfixOperatorToken extends Token{
    private BinaryOperator<Double> operator;
    private int priority;
    private boolean leftAssociative;

    public InfixOperatorToken(String name_, BinaryOperator<Double> operator_, int priority_, boolean isLeftAssociative){
        super(name_);
        operator = operator_;
        priority = priority_;
        leftAssociative = isLeftAssociative;
    }

    public int getPriority(){
        return priority;
    }
    public boolean isLeftAssociative(){
        return leftAssociative;
    }
    public BinaryOperator<Double> getOperator(){
        return operator;
    }

    @Override
    public String toString(){
        return "INFIX_OPERATOR_TOKEN: " + getName();
    }
}
