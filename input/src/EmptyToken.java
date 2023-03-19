public class EmptyToken extends Token{
    EmptyToken(){
        super("");
    }

    @Override
    public String toString(){
        return "EMPTY_TOKEN";
    }
}
