import java.util.List;

public class Calculator {
    private Tokenizer tokenizer;
    private ExpressionTreeBuilder treeBuilder;

    public Calculator(){
        tokenizer = new Tokenizer();
        treeBuilder = new ExpressionTreeBuilder();

        setVariable("PI", Math.PI);
        setVariable("E", Math.E);

        tokenizer.addFunction("sin", Math::sin);
        tokenizer.addFunction("cos", Math::cos);
        tokenizer.addFunction("ln", Math::log);
    }

    public void doCommand(String command){
        if(command.startsWith("set")){ // ==================================
            String assignment = command.substring(3).trim();
            if(assignment.contains("=")){
                int eqPos = assignment.indexOf("=");
                String name = assignment.substring(0, eqPos).trim();
                String expr = assignment.substring(eqPos + 1).trim();
                double value;

                try{
                    value = evaluateString(expr);
                }
                catch(Exception e){
                    System.out.println("ERROR!");
                    System.out.println(e.getMessage());
                    return;
                }

                setVariable(name, value);


            }else{
                System.out.println("Invalid syntax of variable assignment!");
                System.out.println("Usage example: set pi = 3.14");
            }
        }
        else if(command.startsWith("tree")){ // =================================
            String expr = command.substring(4).trim();

            printTree(expr);
        }
        else{ // =================================================
            double res;
            try{
                res = evaluateString(command);
            }
            catch(Exception e){
                System.out.println("ERROR!");
                System.out.println(e.getMessage());
                return;
            }

            System.out.println(
                    String.format("%.5f", res)
            );
        }
    }

    public double evaluateString(String expr) throws Exception {
        List<Token> tokens = tokenizer.tokenize(expr);
        ExpressionNode root = treeBuilder.buildExpressionTree(tokens);
        double result = root.evaluate();

        return result;
    }

    private void printTree(String expr){
        try {
            List<Token> tokens = tokenizer.tokenize(expr);
            ExpressionNode root = treeBuilder.buildExpressionTree(tokens);
            root.print(0);
        }
        catch(Exception e){
            System.out.println("Error getting tree!");
            System.out.println(e.getMessage());
        }
    }

    private void setVariable(String name, double value){
        try{
            tokenizer.setVariable(name, value);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
