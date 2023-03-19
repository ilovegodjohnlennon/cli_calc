public class LiteralToken extends Token{
    private double value;

    public LiteralToken(double value_){
        super("");
        value = value_;
    }

    @Override
    public String toString(){
        return "LITERAL_TOKEN: " + value;
    }
}
