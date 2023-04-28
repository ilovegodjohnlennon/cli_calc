import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        calculator
                .setVariable("PI"  , Math.PI   )
                .setVariable("E"   , Math.E    )

                .setFunction("sin" , Math::sin )
                .setFunction("cos" , Math::cos )
                .setFunction("tan" , Math::tan )
                .setFunction("sqrt", Math::sqrt)
                .setFunction("ln"  , Math::log )
                .setFunction("exp" , Math::exp )

                .run();


        System.out.println("\n\nGoodbye!");
    }
}