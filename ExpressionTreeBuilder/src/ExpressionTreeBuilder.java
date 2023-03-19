import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ExpressionTreeBuilder {

    // returns root of the tree
    public ExpressionNode buildExpressionTree(List<Token> tokens) throws Exception {
        Stack<Token> opStack = new Stack<Token>();
        Stack<ExpressionNode> resultStack = new Stack<ExpressionNode>();

        for(Token currentToken: tokens){
            if(currentToken instanceof LiteralToken){
                resultStack.push(new LiteralNode((LiteralToken) currentToken));
                continue;
            }
            if(currentToken instanceof VariableToken){
                resultStack.push(new VariableNode((VariableToken) currentToken));
                continue;
            }
            if(currentToken instanceof InfixOperatorToken){
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
            throw new Exception("Error constructing tree! This token should not be in the opStack: " + opToken.toString());
        }

        if(resultStack.isEmpty()){
            throw new Exception("Expression is empty!");
        }
        return resultStack.pop();
    }



}
