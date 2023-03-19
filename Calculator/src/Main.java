import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean running = true;
        Calculator calculator = new Calculator();
        DecimalFormat decimalFormat = new DecimalFormat("0.0000000000");
        Scanner scanner = new Scanner(System.in);
        String input;
        double result;

        System.out.println("==== Welcome to the calculator! ====");
        System.out.println("Enter expression to evaluate or 'q' to quit\n\n");

        while(running){
            System.out.print(">>> ");
            input = scanner.nextLine();
            if(input.equals("q")){
                running = false;
                break;
            }

            try{
                result = calculator.evaluateString(input);
            }
            catch(Exception e){
                System.out.println("ERROR!");
                System.out.println(e.toString());
                continue;
            }

            System.out.println(decimalFormat.format(result));

        }

        System.out.println("\n\nGoodbye!");
    }
}