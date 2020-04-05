package simplifier.chain;

import simplifier.expression.Expression;

final public class MapCall extends Call {


    /**
     * Map call"s constructor
     *
     * @param expression call's expression, return number
     */
    public MapCall(Expression expression) {
        super(expression);
    }

    @Override
    public String toString() {
        return "map{".concat(expression.toString()).concat("}");
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
