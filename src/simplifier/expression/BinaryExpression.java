package simplifier.expression;

import simplifier.exceptions.TypeException;

import java.util.Objects;

public class BinaryExpression extends Expression {
    private final Operation operation;
    private Expression leftOperand, rightOperand;

    private void typeChecker() throws TypeException {
        if (operation.getOperandsType() != leftOperand.getReturnedType()
                || operation.getOperandsType() != rightOperand.getReturnedType()) {
            throw new TypeException(operation.toString() + " has incorrect operands:" + System.lineSeparator()
                    + "Left:" + leftOperand.toString() + System.lineSeparator()
                    + "Right" + rightOperand.toString());
        }
    }

    /**
     * Create a new binary expression
     *
     * @param operation operation for expression
     * @param leftOperand left expression
     * @param rightOperand right expression
     */
    public BinaryExpression(Operation operation, Expression leftOperand, Expression rightOperand) {
        super();
        this.operation = operation;
        returnedType = operation.getReturnedType();
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    /**
     * Check types for operation.
     *
     * @throws TypeException If types of left expression or right expression incorrect for operation
     */
    public void typeCheck() throws TypeException {
        typeChecker();
    }

    private boolean checkForZero(Expression checkedExpression) {
        return (checkedExpression instanceof ConstExpression
                && ((ConstExpression) checkedExpression).getNumber() == 0);
    }

    @Override
    public Expression apply(Expression expression) {
        leftOperand = leftOperand.apply(expression);
        rightOperand = rightOperand.apply(expression);
        return this;
    }

    @Override
    public String toString() {
        if (operation == Operation.MUL && (checkForZero(leftOperand) || checkForZero(rightOperand))) {
            return "0";
        }
        if ((operation == Operation.SUM || operation == Operation.DIF) && checkForZero(rightOperand)) {
            return leftOperand.toString();
        }
        if ((operation == Operation.SUM || operation == Operation.DIF) && checkForZero(leftOperand)) {
            return rightOperand.toString();
        }
        return "(".concat(leftOperand.toString())
                .concat(operation.toString())
                .concat(rightOperand.toString()).concat(")");
    }

    public Operation getOperation() {
        return operation;
    }

    public Expression getLeftOperand() {
        return leftOperand;
    }

    public Expression getRightOperand() {
        return rightOperand;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryExpression that = (BinaryExpression) o;
        return operation == that.operation &&
                leftOperand.equals(that.leftOperand) &&
                rightOperand.equals(that.rightOperand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operation, leftOperand, rightOperand);
    }
}
