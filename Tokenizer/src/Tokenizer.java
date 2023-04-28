import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

/*

    Scanning the expression string into a list of tokens first probably isn't even necessary.
    Should think about scanning into the tree right away -_-

 */

/*

    Current algorithm for determining whether a '+' or '-' is unary or binary:
        IF last token was either CLOSING_BRACKET, LITERAL or VARIABLE -> THEN binary
        OTHERWISE, unary

 */

public class Tokenizer {
    private Map<String, Token> tokenMap;
    private Token lastToken;

    // read next token from string, starting from given pos.
    // returns the scanned token and start pos for next token
    private Pair<Token, Integer> nextToken(String input, int startPos) throws Exception {
        int i = startPos;
        char c;
        Pair<Token, Integer> result = null;

        // discard any whitespaces
        while(i < input.length()) {
            c = input.charAt(i);
            if (c != ' ') break;
            i++;
        }


        // check end of input string
        if(i >= input.length()){
            result = new Pair<Token, Integer>(new EmptyToken(), i);
            return result;
        }

        // scan next character.
        // use it to determine whether next token will be a literal or a name
        c = input.charAt(i);

        if(isValidTokenNameFirstChar(c)){
            result = nextNamedToken(input, i);
            return result;
        }
        // if c is a digit, then next token is a literal
        if((c <= '9') && (c >= '0')){
            result = nextNumberLiteral(input, i);
            return result;
        }

        // special case: tokens whose name is only 1 single char
        // can scan them right from here using switch ^_^
        //
        // but first, move index to next char
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





        // normally, function should have returned before this point


        throw new Exception("Encountered weird character at pos: " + i);

    }

    // call from nextToken() if next char is alphabetical.
    // i.e. the token is a variable or function name
    private Pair<Token, Integer> nextNamedToken(String input, int startPos) throws Exception {
        int i = startPos;
        // use StringBuilder instead of String because the IDE told me so -_-
        StringBuilder name = new StringBuilder();
        char c;

        for( ; i < input.length(); i++){
            c = input.charAt(i);
            if(!isValidTokenNameChar(c)){
                break;
            }
            name.append(c);
        }

        if(!tokenMap.containsKey(name.toString())){
            throw new Exception("Symbol " + name + " does not exist!");
        }

        return new Pair<Token, Integer>(tokenMap.get(name.toString()), i);
    }

    // call from nextToken() if next char is a digit.
    private Pair<Token, Integer> nextNumberLiteral(String input, int startPos){
        int i = startPos;
        double scannedNumber = 0.0;
        double decimalPos = 0.1;
        char c;

        // this is probably unnecessarily complicated -_-
        // should have used Scanner.nextDouble or smth, idk
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





    // the only function that should be called from outside
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

    public void setVariable(String name, double value){
        if(!isValidTokenName(name)){
            throw new IllegalArgumentException("Invalid variable name: " + name);
        }

        if(tokenMap.containsKey(name)){
            Token t = tokenMap.get(name);

            if(t instanceof VariableToken){
                ((VariableToken) t).setValue(value);
            }
            else{
                throw new IllegalArgumentException("Error! " + name + " is not a variable!");
            }
        }
        else{
            tokenMap.put(name, new VariableToken(name, value));
        }
    }

    public void addFunction(String name, UnaryOperator<Double> function){
        if(!isValidTokenName(name)){
            throw new IllegalArgumentException("Invalid function name: " + name);
        }

        if(tokenMap.containsKey(name)){
            Token t = tokenMap.get(name);

            if(t instanceof PrefixOperatorToken){
                ((PrefixOperatorToken) t).setOperator(function);
            }
            else{
                throw new IllegalArgumentException("Error! " + name + " already exists and is not a function!");
            }
        }
        else{
            tokenMap.put(name, new PrefixOperatorToken(name, function));
        }
    }

    public Tokenizer(){
        tokenMap = new HashMap<String, Token>();
        lastToken = null;


        // ====================== INFIX OPERATORS  ============================
        tokenMap.put("+", new InfixOperatorToken("+", (x, y) -> x + y, 10, true));
        tokenMap.put("-", new InfixOperatorToken("-", (x, y) -> x - y, 10, true));
        tokenMap.put("*", new InfixOperatorToken("*", (x, y) -> x * y, 20, true));
        tokenMap.put("/", new InfixOperatorToken("/", (x, y) -> x / y, 20, true ));

        // ====================== PREFIX OPERATORS ============================
        tokenMap.put("PREFIX_PLUS", new PrefixOperatorToken("PREFIX_PLUS", x -> x));
        tokenMap.put("PREFIX_MINUS", new PrefixOperatorToken("PREFIX_MINUS", x -> -x));
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

    private static boolean isValidTokenName(String str){
        if(str.isBlank()) return false;
        if(!isValidTokenNameFirstChar(str.charAt(0))) return false;

        for(int i = 1; i < str.length(); i++){
            if(!isValidTokenNameChar(str.charAt(i))) return false;
        }

        return true;
    }
}
