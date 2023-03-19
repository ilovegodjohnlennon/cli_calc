public abstract class Token {
    private String name;

    public Token(String name_){
        name = name_;
    }

    public String getName(){
        return name;
    }
}
