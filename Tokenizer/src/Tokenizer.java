import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Tokenizer {
    private Map<String, Token> tokenMap;
    private Token lastToken;

    // read next token from string, starting from given pos.
    // returns the scanned token and start pos for next token
    private Pair<Token, Integer> nextToken(String input, int startPos) throws Exception {
        int i = startPos;
        char c;
        Pair<Token, Integer> result = null;

        while(i < input.length()) {
            c = input.charAt(i);
            if (c != ' ') break;
            i++;
        }


        if(i >= input.length()){
            result = new Pair<Token, Integer>(new EmptyToken(), i);
            return result;
        }
        c = input.charAt(i);

        if(isValidTokenNameFirstChar(c)){
            result = nextNamedToken(input, i);
            return result;
        }
        if((c <= '9') && (c >= '0')){
            result = nextNumberLiteral(input, i);
            return result;
        }

        i++;
        switch(c){
            case '(':
                result = new Pair<Token, Integer>(new OpeningBracketToken(), i);
                return result;
            case ')':
                result = new Pair<Token, Integer>(new ClosingBracketToken(), i);
                return result;
            case '+':
                if(lastToken instanceof ClosingBracketToken || lastToken instanceof LiteralToken || lastToken instanceof VariableToken){
                    result = new Pair<Token, Integer>(tokenMap.get("+"), i);
                }
                else{
                    result = new Pair<Token, Integer>(tokenMap.get("PREFIX_PLUS"), i);
                }
                return result;
            case '-':
                if(lastToken instanceof ClosingBracketToken || lastToken instanceof LiteralToken || lastToken instanceof VariableToken){
                    result = new Pair<Token, Integer>(tokenMap.get("-"), i);
                }
                else{
                    result = new Pair<Token, Integer>(tokenMap.get("PREFIX_MINUS"), i);
                }
                return result;
            case '*':
                result = new Pair<Token, Integer>(tokenMap.get("*"), i);
                return result;
            case '/':
                result = new Pair<Token, Integer>(tokenMap.get("/"), i);
                return result;
            default:
                break;




        }








        throw new Exception("Encountered weird character at pos: " + i);

    }

    // call from nextToken() if next char is alphabetical.
    // i.e. the token is a variable or function name
    private Pair<Token, Integer> nextNamedToken(String input, int startPos) throws Exception {
        int i = startPos;
        String name = "";
        char c;

        for( ; i < input.length(); i++){
            c = input.charAt(i);
            if(!isValidTokenNameChar(c)){
                break;
            }
            name += c;
        }

        if(!tokenMap.containsKey(name)){
            throw new Exception("Symbol " + name + " does not exist!");
        }

        return new Pair<Token, Integer>(tokenMap.get(name), i);
    }

    // call from nextToken() if next char is a digit.
    private Pair<Token, Integer> nextNumberLiteral(String input, int startPos){
        int i = startPos;
        double scannedNumber = 0.0;
        double decimalPos = 0.1;
        char c;

        for( ; i < input.length(); i++){
            c = input.charAt(i);
            if((c >= '0') && (c <= '9')){
                scannedNumber *= 10.0;
                scannedNumber += Character.getNumericValue(c);
            }
            else if(c == '.'){
                i++;
                for( ; i < input.length(); i++){
                    c = input.charAt(i);
                    if((c >= '0') && (c <= '9')){
                        scannedNumber += decimalPos * Character.getNumericValue(c);
                        decimalPos *= 0.1;
                    }
                    else break;

                }
                break;
            }
            else break;
        }

        return new Pair<Token, Integer>(new LiteralToken(scannedNumber), i);
    }






    public List<Token> tokenize(String input) throws Exception {
        List<Token> result = new LinkedList<Token>();
        lastToken = null;
        int inputPos = 0;


        Pair<Token, Integer> lastUnit = nextToken(input, inputPos);
        lastToken = lastUnit.first;
        inputPos = lastUnit.second;
        result.add(lastToken);

        while(!(lastToken instanceof EmptyToken)){
            lastUnit = nextToken(input, inputPos);
            lastToken = lastUnit.first;
            inputPos = lastUnit.second;
            result.add(lastToken);
        }

        return result;
    }

    public Tokenizer(){
        tokenMap = new HashMap<String, Token>();
        lastToken = null;

        // ====================== USEFUL CONSTANTS ============================
        tokenMap.put("PI", new VariableToken("PI", 3.14159265));
        tokenMap.put("E", new VariableToken("E", 2.718281828));

        // ====================== INFIX OPERATORS  ============================
        tokenMap.put("+", new InfixOperatorToken("+", (x, y) -> x + y, 10));
        tokenMap.put("-", new InfixOperatorToken("-", (x, y) -> x - y, 10));
        tokenMap.put("*", new InfixOperatorToken("*", (x, y) -> x * y, 20));
        tokenMap.put("/", new InfixOperatorToken("/", (x, y) -> x / y, 20));

        // ====================== PREFIX OPERATORS ============================
        tokenMap.put("PREFIX_PLUS", new PrefixOperatorToken("PREFIX_PLUS", x -> x));
        tokenMap.put("PREFIX_MINUS", new PrefixOperatorToken("PREFIX_MINUS", x -> -x));
        tokenMap.put("sin", new PrefixOperatorToken("sin", x -> Math.sin(x)));
    }

    // only latin alphabet, digits and underscores are valid in names
    private static boolean isValidTokenNameChar(char c){
        boolean isLower = c >= 'a' && c <= 'z';
        boolean isUpper = c >= 'A' && c <= 'Z';
        boolean isDigit = c >= '0' && c <= '9';

        return isLower || isUpper || isDigit || (c == '_');
    }

    // name can only start with latin letter or underscore
    private static boolean isValidTokenNameFirstChar(char c){
        boolean isLower = c >= 'a' && c <= 'z';
        boolean isUpper = c >= 'A' && c <= 'Z';

        return isLower || isUpper || (c == '_');
    }
}
