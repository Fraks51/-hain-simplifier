package simplifier.expression;

import simplifier.exceptions.TypeException;

import java.lang.reflect.Type;
import java.util.Objects;

public abstract class Expression {
    protected Expression element;
    protected Type returnedType;

    protected Expression() {
        element = null;
    }

    public Type getReturnedType() {
        return returnedType;
    }

    public abstract Expression apply(Expression expression);

    public Expression concat(Expression expression) throws TypeException {
        return new BinaryExpression(Operation.AND, this, expression);
    }
    
    public abstract String toString();

    @Override
    public abstract boolean equals(Object o);

    public Expression getElement() {
        return element;
    }
}
