import java.util.List;
import java.util.Map;
import java.util.Stack;

// this is the most disgusting code :(
// should rewrite prettier
public class ExpressionTreeBuilder {

    // state of the expression tree builder
    private Stack<Token> opStack;
    private Stack<ExpressionNode> resultStack;

    public ExpressionTreeBuilder(){
        opStack = new Stack<>();
        resultStack = new Stack<>();
    }

    public void resetState(){
        opStack.clear();
        resultStack.clear();
    }

    // returns root of the tree
    public ExpressionNode buildExpressionTree(List<Token> tokens) throws Exception {
        resetState();

        // go through the token list one by one
        for(Token currentToken: tokens){
            // if token is literal or variable, just push it to result stack.
            // simple
            if(currentToken instanceof LiteralToken){
                resultStack.push(new LiteralNode((LiteralToken) currentToken));
                continue;
            }
            if(currentToken instanceof VariableToken){
                resultStack.push(new VariableNode((VariableToken) currentToken));
                continue;
            }

            // if token is an operator, this gets more complicated...

            /* =====================================================
                GENERAL RULES FOR OPERATORS:

                    0. PREFIX_OPERATOR always has higher priority than INFIX_OPERATOR

                    1. If the operator on top of the opStack has higher priority than currentToken:
                        - pop this operator from the opStack and push to resultStack...
                        - push currentToken to opStack
                    2. If top of the opStack has lower priority than currentToken...
                        then just push currentToken to opStack
                    3. If priorities are equal, everything should be decided by associativity...
                        which I haven't implemented yet.
                        So far everything is left-associative.
               ===================================================== */
            if(currentToken instanceof InfixOperatorToken){
                processInfixToken((InfixOperatorToken) currentToken);
                continue;
            }
            // prefix op. always has higher priority, so we push it. easy.
            if(currentToken instanceof PrefixOperatorToken){
                opStack.push(currentToken);
                continue;
            }
            if(currentToken instanceof OpeningBracketToken){
                opStack.push(currentToken);
                continue;
            }
            if(currentToken instanceof ClosingBracketToken){

                collapseOpStack();
                continue;
            }

        }

        // basically collapsing the stack after all tokens have been read from the input.
        collapseOpStack();

        if(!opStack.isEmpty()){
            throw new IllegalStateException("Error! opStack is not empty at the end of constructing tree! Probably a mismatched bracket...");
        }

        // this should never happen because I'm catching blank strings in Main
        if(resultStack.isEmpty()){
            throw new Exception("Expression is empty!");
        }
        if(resultStack.size() > 1){
            throw new Exception("Something wrong, i can feel it..");
        }
        return resultStack.pop();
    }

    // just pop tokens from the opstack and immediately push them to result
    // until opstack is empty or an opening bracket is encountered.
    private void collapseOpStack() throws Exception {
        while(!opStack.isEmpty()){
            Token opToken = opStack.pop();

            if(opToken instanceof OpeningBracketToken){
                return;
            }
            else if(opToken instanceof PrefixOperatorToken){
                pushPrefixToResult((PrefixOperatorToken) opToken);
            }
            else if(opToken instanceof InfixOperatorToken){
                pushInfixToResult((InfixOperatorToken) opToken);
            }
            else{
                throw new IllegalStateException("ERROR! Encountered weird token while collapting opStack: " + opToken.toString());
            }


        }
    }

    private void processInfixToken(InfixOperatorToken currentToken) throws Exception {
        if(opStack.isEmpty() || opStack.peek() instanceof OpeningBracketToken){
            opStack.push(currentToken);
            return;
        }

        // need to pop all the operators that have greater precedence!!
        while(!opStack.isEmpty()){
            Token tok = opStack.peek();

            if(tok instanceof PrefixOperatorToken){
                pushPrefixToResult((PrefixOperatorToken) opStack.pop());
            }
            else if(tok instanceof InfixOperatorToken && ((InfixOperatorToken) tok).getPriority() > currentToken.getPriority()){
                pushInfixToResult((InfixOperatorToken)opStack.pop());
            }
            else{
                break;
            }
        }

        opStack.push(currentToken);

    }

    private void pushPrefixToResult(PrefixOperatorToken prefixToken) throws Exception {
        if(resultStack.isEmpty()){
            throw new Exception("No argument for unary operator: " + prefixToken.toString());
        }

        ExpressionNode arg = resultStack.pop();
        resultStack.push(new UnaryOperatorNode(prefixToken, arg));
    }

    private void pushInfixToResult(InfixOperatorToken infixToken) throws Exception {
        if(resultStack.size() < 2){
            throw new Exception("Not enough arguments for binary operator: " + infixToken.toString());
        }

        ExpressionNode arg2 = resultStack.pop();
        ExpressionNode arg1 = resultStack.pop();

        resultStack.push(new BinaryOperatorNode(infixToken, arg1, arg2));
    }




}
