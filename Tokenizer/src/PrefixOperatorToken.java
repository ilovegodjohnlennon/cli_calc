import java.util.function.UnaryOperator;

public class PrefixOperatorToken extends Token{
    private UnaryOperator<Double> operator;

    public PrefixOperatorToken(String name_, UnaryOperator<Double> operator_){
        super(name_);
        operator = operator_;
    }

    public UnaryOperator<Double> getOperator(){
        return operator;
    }
    public void setOperator(UnaryOperator<Double> op){
        operator = op;
    }

    @Override
    public String toString(){
        return "PREFIX_OPERATOR_TOKEN: " + getName();
    }
}
