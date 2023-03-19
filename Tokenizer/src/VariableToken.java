public class VariableToken extends Token{
    private double value;

    public VariableToken(String name_, double value_){
        super(name_);
        value = value_;
    }

    public double getValue(){
        return value;
    }

    @Override
    public String toString(){
        return "VARIABLE_TOKEN: " + getName() + " : " + value;
    }
}
