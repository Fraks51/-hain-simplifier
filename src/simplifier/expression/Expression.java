package simplifier.expression;

import java.lang.reflect.Type;

public abstract class Expression {
    protected Type returnedType;

    /**
     * Create empty expression
     */
    protected Expression() { }

    /**
     * Get the return type of an expression.
     * @return return type of an expression
     */
    public Type getReturnedType() {
        return returnedType;
    }

    /**
     * Replace all elements with the given expression and return a new expression
     *
     * @param expression target to re
     * @return new {@link Expression}
     */
    public abstract Expression apply(Expression expression);

    /**
     * Logic join a two boolean expression by Operation.AND
     * and return this new logic expression
     * @param expression target to join
     * @return joined expression
     */
    public Expression join(Expression expression) {
        return new BinaryExpression(Operation.AND, this, expression);
    }

    /**
     * Get expression in string format
     *
     * @return expression in string format
     */
    public abstract String toString();

    @Override
    public abstract boolean equals(Object o);

}
