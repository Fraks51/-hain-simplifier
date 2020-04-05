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
        if (element == null) {
            return "element";
        } else  {
            return element.toString();
        }
    }

    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        return obj instanceof ElementExpression;
    }
}
