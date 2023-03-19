import java.util.List;

public class Main {
    public static void main(String[] args) {
        Tokenizer tk = new Tokenizer();
        ExpressionTreeBuilder etb = new ExpressionTreeBuilder();
        String expr = "2 * sin(3 + 1 * PI)";
        List<Token> tokens;
        ExpressionNode root;

        try{
            tokens = tk.tokenize(expr);
        }
        catch(Exception e){
            System.out.println(e.toString());
            return;
        }

        try{
            root = etb.buildExpressionTree(tokens);
        }
        catch(Exception e){
            System.out.println(e.toString());
            return;
        }

        System.out.println("Tree of expression: " + expr);
        root.print(0);
        System.out.println("Result: " + root.evaluate());

    }
}