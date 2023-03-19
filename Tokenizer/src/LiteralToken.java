public class LiteralToken extends Token{
    private double value;

    public LiteralToken(double value_){
        super("");
        value = value_;
    }

    public double getValue(){
        return value;
    }

    @Override
    public String toString(){
        return "LITERAL_TOKEN: " + value;
    }
}
