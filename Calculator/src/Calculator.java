import java.util.List;

public class Calculator {
    private Tokenizer tokenizer;
    private ExpressionTreeBuilder treeBuilder;

    public Calculator(){
        tokenizer = new Tokenizer();
        treeBuilder = new ExpressionTreeBuilder();
    }

    public double evaluateString(String expr) throws Exception {
        List<Token> tokens = tokenizer.tokenize(expr);
        ExpressionNode root = treeBuilder.buildExpressionTree(tokens);
        double result = root.evaluate();

        return result;
    }
}
