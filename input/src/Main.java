import java.util.List;

public class Main {
    public static void main(String[] args) {
        Tokenizer tk = new Tokenizer();
        List<Token> res;

        try{
            res = tk.tokenize("sin(2 ) / sin(2 / PI) - E * (-8 - 1)");

        }
        catch(Exception e){
            System.out.println(e.toString());
            return;
        }

        for(Token token: res){
            System.out.println(token.toString());
        }

    }
}