package simplifier.chain;

import simplifier.expression.Expression;

final public class FilterCall extends Call {

    /**
     * Filter call"s constructor
     *
     * @param expression call's expression, return true or false for element
     */
    public FilterCall(Expression expression) {
        super(expression);
    }

    @Override
    public String toString() {
        return "filter{".concat(expression.toString()).concat("}");
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
