import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean running = true;
        Calculator calculator = new Calculator();
        Scanner scanner = new Scanner(System.in);
        String input;
        double result;

        System.out.println("==== Welcome to the calculator! ====");
        System.out.println("Enter expression to evaluate or 'q' to quit\n\n");

        while(running){
            System.out.print(">>> ");
            input = scanner.nextLine().trim();

            if(input.isBlank()){
                continue;
            }
            if(input.equals("q")){
                running = false;
                break;
            }

            calculator.doCommand(input);

        }

        System.out.println("\n\nGoodbye!");
    }
}