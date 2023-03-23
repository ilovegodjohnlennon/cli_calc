import java.util.List;
import java.util.Map;
import java.util.Stack;

// this is the most disgusting code :(
// should rewrite prettier
public class ExpressionTreeBuilder {

    // returns root of the tree
    public ExpressionNode buildExpressionTree(List<Token> tokens) throws Exception {
        Stack<Token> opStack = new Stack<Token>();
        Stack<ExpressionNode> resultStack = new Stack<ExpressionNode>();

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
                // if opStack is empty or has OPENING_BRACKET on top, just push currentToken on it.
                if(opStack.isEmpty() || opStack.peek() instanceof OpeningBracketToken){
                    opStack.push(currentToken);
                    continue;
                }


                if(opStack.peek() instanceof PrefixOperatorToken){
                    PrefixOperatorToken prefToken = (PrefixOperatorToken) opStack.pop();
                    opStack.push(currentToken);
                    if(resultStack.isEmpty()){
                        throw new Exception("Unary operator without argument: " + prefToken.toString());
                    }
                    ExpressionNode arg = resultStack.pop();
                    resultStack.push(new UnaryOperatorNode(prefToken, arg));
                    continue;
                }
                if(opStack.peek() instanceof InfixOperatorToken){
                    if(((InfixOperatorToken) currentToken).getPriority() > ((InfixOperatorToken) opStack.peek()).getPriority()){
                        opStack.push(currentToken);
                        continue;
                    }
                    InfixOperatorToken infixToken = (InfixOperatorToken) opStack.pop();
                    opStack.push(currentToken);
                    if(resultStack.size() < 2){
                        throw new Exception("Not enough arguments for binary operator: " + infixToken.toString());
                    }
                    ExpressionNode arg2 = resultStack.pop();
                    ExpressionNode arg1 = resultStack.pop();
                    resultStack.push(new BinaryOperatorNode(infixToken, arg1, arg2));
                    continue;
                }
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
                while(!opStack.isEmpty()){
                    Token opToken = opStack.pop();
                    if(opToken instanceof OpeningBracketToken){
                        break;
                    }
                    if(opStack.isEmpty()){
                        throw new Exception("No matching ( bracket!");
                    }
                    if(opToken instanceof PrefixOperatorToken){
                        if(resultStack.isEmpty()){
                            throw new Exception("Unary operator without argument: " + opToken.toString());
                        }
                        ExpressionNode arg = resultStack.pop();
                        resultStack.push(new UnaryOperatorNode((PrefixOperatorToken) opToken, arg));
                        continue;
                    }
                    if(opToken instanceof InfixOperatorToken){
                        if(resultStack.size() < 2){
                            throw new Exception("Not enough arguments for binary operator: " + opToken.toString());
                        }
                        ExpressionNode arg2 = resultStack.pop();
                        ExpressionNode arg1 = resultStack.pop();
                        resultStack.push(new BinaryOperatorNode((InfixOperatorToken) opToken, arg1, arg2));
                        continue;
                    }

                }
                continue;
            }

        }

        // basically collapsing the stack after all tokens have been read from the input.
        while(!opStack.isEmpty()){
            Token opToken = opStack.pop();

            if(opToken instanceof PrefixOperatorToken){
                if(resultStack.isEmpty()){
                    throw new Exception("No argument for unary operator: " + opToken.toString());
                }
                ExpressionNode arg = resultStack.pop();
                resultStack.push(new UnaryOperatorNode((PrefixOperatorToken) opToken, arg));
                continue;
            }
            if(opToken instanceof InfixOperatorToken){
                if(resultStack.size() < 2){
                    throw new Exception("Not enough arguments for binary operator: " + opToken.toString());
                }
                ExpressionNode arg2 = resultStack.pop();
                ExpressionNode arg1 = resultStack.pop();
                resultStack.push(new BinaryOperatorNode((InfixOperatorToken) opToken, arg1, arg2));
                continue;
            }
            if(opToken instanceof OpeningBracketToken){
                throw new Exception("Error constructing tree! Found lonely opening bracket.");
            }

            throw new Exception("Error constructing tree! This token should not be in the opStack: " + opToken.toString());
        }

        // this should never happen because I'm catching blank strings in Main
        if(resultStack.isEmpty()){
            throw new Exception("Expression is empty!");
        }
        return resultStack.pop();
    }



}
