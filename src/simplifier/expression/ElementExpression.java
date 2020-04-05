package simplifier.expression;

final public class ElementExpression extends Expression {

    public ElementExpression() {
        super();
        returnedType = Integer.TYPE;
    }

    @Override
    public Expression apply(Expression expression) {
        if (expression == null) {
            return this;
        }
        return expression;
    }

    public String toString() {
            return "element";
    }

    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        return obj instanceof ElementExpression;
    }
}
