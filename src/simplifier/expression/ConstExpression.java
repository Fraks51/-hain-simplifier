package simplifier.expression;

import java.util.Objects;

final public class ConstExpression extends Expression {
    private int number;

    public ConstExpression(int number) {
        super();
        this.number = number;
        returnedType = Integer.TYPE;
    }

    @Override
    public Expression apply(Expression expression) {
        return this;
    }

    @Override
    public String toString() {
        return Integer.toString(number);
    }

    public int getNumber() {return number;}

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstExpression that = (ConstExpression) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
