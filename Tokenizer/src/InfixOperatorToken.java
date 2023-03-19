import java.util.function.BinaryOperator;

// INFIX OPERATORS HAVE PRIORITY!!!!
// +, -: 10
// *, /: 20
public class InfixOperatorToken extends Token{
    private BinaryOperator<Double> operator;
    private int priority;

    public InfixOperatorToken(String name_, BinaryOperator<Double> operator_, int priority_){
        super(name_);
        operator = operator_;
        priority = priority_;
    }

    public int getPriority(){
        return priority;
    }
    public BinaryOperator<Double> getOperator(){
        return operator;
    }

    @Override
    public String toString(){
        return "INFIX_OPERATOR_TOKEN: " + getName();
    }
}
