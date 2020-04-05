package simplifier.chain;

import simplifier.expression.Expression;

public abstract class Call {
    /**
     * Call expression
     */
    protected Expression expression;

    /**
     * Call's constructor
     *
     * @param expression set <var>expression</var>
     */
    protected Call(Expression expression) {
        this.expression = expression;
    }

    /**
     * Return <var>expression</var>
     *
     * @return expression for this call
     */
    public Expression getExpression() {
        return expression;
    }

    /**
     * Get string format of call
     *
     * @return {@link String} of the current call
     */
    public abstract String toString();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Call) {
            return expression.equals(((Call) obj).getExpression());
        }
        return false;
    }
}
