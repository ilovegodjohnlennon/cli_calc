import java.util.List;
import java.util.Scanner;
import java.util.function.UnaryOperator;

public class Calculator {
    private Tokenizer tokenizer;
    private ExpressionTreeBuilder treeBuilder;
    private Scanner scanner;
    private UserHelper helper;
    private boolean running;

    public Calculator(){
        tokenizer = new Tokenizer();
        treeBuilder = new ExpressionTreeBuilder();
        scanner = new Scanner(System.in);
        helper = new UserHelper();

        running = true;
    }

    public void doCommand(String command){
        if(command.isBlank()){
            return;
        }
        else if(command.equals("q")){
            running = false;
            return;
        }
        else if(command.startsWith("help")){
            String query = command.substring(4).trim();
            if(query.isBlank()){
                helper.genericHelp();
                return;
            }

            helper.showEntry(query);
        }
        else if(command.startsWith("set")){ // ==================================
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

    public Calculator setVariable(String name, double value){
        try{
            tokenizer.setVariable(name, value);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        return this;
    }

    public Calculator setFunction(String name, UnaryOperator<Double> function){
        try{
            tokenizer.addFunction(name, function);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        return this;
    }


    public void run(){
        String input;

        helper.greet();

        while(running){
            System.out.print(">>> ");

            input = scanner.nextLine().trim();
            doCommand(input);
        }
    }
}
