public class VariableNode extends ExpressionNode{
    private double value;

    public VariableNode(VariableToken token){
        super(token.getName());
        value = token.getValue();
    }

    @Override
    public double evaluate(){
        return value;
    }
    @Override
    public void print(int depth){
        for(int i = 0; i < depth; i++) System.out.print(' ');
        System.out.println("VARIABLE: " + getName() + " : " + value);
    }
}
