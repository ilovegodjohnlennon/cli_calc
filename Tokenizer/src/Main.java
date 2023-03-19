import java.util.List;

public class Main {
    public static void main(String[] args) {
        Tokenizer tk = new Tokenizer();
        List<Token> result;

        try{
            result = tk.tokenize("-sin(2 * PI) / 7.1");
        }
        catch(Exception e){
            System.out.println(e.toString());
            return;
        }

        for(Token t: result){
            System.out.println(t.toString());
        }

    }
}