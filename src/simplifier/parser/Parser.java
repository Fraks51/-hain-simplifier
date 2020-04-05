package simplifier.parser;

import simplifier.chain.Call;
import simplifier.chain.Chain;
import simplifier.chain.FilterCall;
import simplifier.chain.MapCall;
import simplifier.exceptions.SyntaxException;
import simplifier.exceptions.TypeException;
import simplifier.expression.*;

public class Parser {

    private static Expression parseExpression(String expression) throws SyntaxException, TypeException {
        if (expression.isEmpty()) {
            throw new SyntaxException("Body of call or simplifier.expression can't be empty");
        }
        if (expression.matches("^element$")) {
            return new ElementExpression();
        }
        if (expression.matches("^-?\\d+$")) {
            return new ConstExpression(Integer.parseInt(expression));
        }
        if (expression.charAt(0) == '(' && expression.charAt(expression.length() - 1) == ')') {
            int operationIndex = -1;
            int bracketBalance = 0;
            for (int i = 0; i < expression.length(); i++) {
                switch (expression.charAt(i)) {
                    case '(':
                        bracketBalance++;
                        break;
                    case ')':
                        bracketBalance--;
                        if (bracketBalance < 0) {
                            throw new SyntaxException("Incorrect bracket sequence:" + expression);
                        }
                        break;
                    case '-':
                        if (bracketBalance == 1 && operationIndex == -1) {
                            if (i == 1) {
                                break;
                            }
                        } else {
                            break;
                        }
                    case '=': case '<': case '>': case '&': case '|': case '*': case '+':
                        if (bracketBalance == 1) {
                            if (operationIndex == -1) {
                                operationIndex = i;
                            }
                        } else if (bracketBalance == 0) {
                            throw new SyntaxException("Binary operation out brackets:" + expression +
                                    System.lineSeparator() + "In " + i + " position");

                        }
                        break;
                }
            }
            if (operationIndex == -1) {
                throw new SyntaxException("Have not binary operation in simplifier.expression:" +
                        System.lineSeparator() + expression);
            }
            Expression leftExpression = parseExpression(expression.substring(1, operationIndex));
            Expression rightExpression = parseExpression(
                    expression.substring(operationIndex + 1, expression.length() - 1)
            );
            BinaryExpression binaryExpression = new BinaryExpression(
                    Operation.fromChar(expression.charAt(operationIndex)),
                    leftExpression, rightExpression
            );
            binaryExpression.typeCheck();
            return binaryExpression;
        }
        throw new SyntaxException("Unknown simplifier.expression:" + expression);
    }

    private static Expression parseExpression(final int shift, String callString) throws SyntaxException, TypeException {
        int size = callString.length();
        if (callString.charAt(shift) == '{' && callString.charAt(size - 1) == '}') {
            return parseExpression(callString.substring(shift + 1, size - 1));
        }
        throw new SyntaxException("Incorrect body of call: " + callString);
    }

    private static Call parseCall(String callString) throws SyntaxException, TypeException {
        if (callString.startsWith("map")) {
            Expression expression = parseExpression(3, callString);
            if (expression.getReturnedType() == Boolean.TYPE) {
                throw new TypeException("Map simplifier.expression returned type can't be boolean");
            }
            return new MapCall(expression);
        } else if (callString.startsWith("filter")) {
            Expression expression = parseExpression(6, callString);
            if (expression.getReturnedType() == Integer.TYPE) {
                throw new TypeException("Filter simplifier.expression returned type can't be integer");
            }
            return new FilterCall(expression);
        }
        throw new SyntaxException("Unknown call:" + callString);
    }

    /**
     * Parsing string as {@link Chain} with maps and filters calls
     *
     * If syntax in string correct, and all calls has a correct
     * expression's types, return it like a chain
     *
     * @param inputString parse target
     * @return {@link Chain} from giving string
     * @throws SyntaxException if string have a wrong syntax
     * @throws TypeException if expression in {@link MapCall} return non number value, or
     * expression in {@link FilterCall} return non boolean value
     */
    public static Chain parseStringToChain(final String inputString) throws SyntaxException, TypeException {
        Chain chain = new Chain();
        if (inputString.equals("")) {
            return chain;
        }
        if (inputString.matches(".*%>%$")) {
            throw new SyntaxException("Operator '%>%' used on the end of string on nothing");
        } // because split() doesn't split last str like "" if regex is suffix
        String[] calls = inputString.split("%>%");
        for (String call : calls) {
            if (call.isEmpty()) {
                throw new SyntaxException("Operator '%>%' used to empty string");
            }
            chain.add(parseCall(call));
        }
        return chain;
    }
}
