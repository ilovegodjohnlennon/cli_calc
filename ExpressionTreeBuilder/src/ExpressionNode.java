public abstract class ExpressionNode {
    private String name;
    public abstract double evaluate();
    public abstract void print(int depth);

    public String getName(){
        return name;
    }
    public ExpressionNode(String name_){
        name = name_;
    }
}

