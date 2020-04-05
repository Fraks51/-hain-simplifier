package simplifier;

import simplifier.chain.Call;
import simplifier.chain.Chain;
import simplifier.chain.FilterCall;
import simplifier.chain.MapCall;
import simplifier.exceptions.SyntaxException;
import simplifier.exceptions.TypeException;
import simplifier.expression.Expression;
import simplifier.parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class ChainSimplifier {
    /**
     * Creat a ChainSimplifier class
     */
    public ChainSimplifier() {}

    /**
     * Parse and simplify string like chain with only 1 filter call
     * and 1 map call
     *
     * @param inputString target to parse and simplify
     * @return {@link Chain} with only 1 filter call
     *      * and 1 map call equivalent incoming chain in string format
     * @throws SyntaxException if string have a wrong syntax
     * @throws TypeException if expression in {@link MapCall} return non number value, or
     *      * expression in {@link FilterCall} return non boolean value
     */
    public Chain applyToChain(String inputString) throws SyntaxException, TypeException {
        Chain chain = Parser.parseStringToChain(inputString);
        List<Expression> filterExpressions = new ArrayList<>();
        Expression mapExpression = null;
        for (Call call : chain.getCalls()) {
            if (call instanceof MapCall) {
                final Expression oldExpression = mapExpression;
                mapExpression = call.getExpression().apply(oldExpression);
            }
            if (call instanceof FilterCall) {
                filterExpressions.add(call.getExpression().apply(mapExpression));
            }
        }
        Chain newChain = new Chain();
        if (!filterExpressions.isEmpty()) {
            Expression expression = filterExpressions.get(0);
            for (int i = 1; i < filterExpressions.size(); i++) {
                expression = expression.join(filterExpressions.get(i));
            }
            newChain.add(new FilterCall(ExpressionSimplifier.simplify(expression)));
        }
        if (mapExpression != null) {
            newChain.add(new MapCall(ExpressionSimplifier.simplify(mapExpression)));
        }
        return newChain;
    }

}
