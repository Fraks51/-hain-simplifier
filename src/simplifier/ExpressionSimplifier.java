package simplifier;

import simplifier.exceptions.TypeException;
import simplifier.expression.*;

import java.util.ArrayList;
import java.util.List;

public class ExpressionSimplifier {

    private final static BinaryExpression constFalse = new BinaryExpression(
                        Operation.EQUAL,
                        new PolynomialExpression(0, List.of(1)),
                        new PolynomialExpression(0, List.of(0))
                );

    /**
     * Always true bool expression
     */
    public final static BinaryExpression constTrue = new BinaryExpression(
            Operation.EQUAL,
            new PolynomialExpression(0, List.of(0)),
            new PolynomialExpression(0, List.of(0))
    );


    private static PolynomialExpression arithmeticSimplify(Expression expression) {
        if (expression instanceof BinaryExpression) {
            BinaryExpression binaryExpression = (BinaryExpression) expression;
            PolynomialExpression leftSimpleExpression = arithmeticSimplify(binaryExpression.getLeftOperand());
            PolynomialExpression rightSimpleExpression = arithmeticSimplify(binaryExpression.getRightOperand());
            switch (binaryExpression.getOperation()) {
                case SUM:
                    return leftSimpleExpression.add(rightSimpleExpression);
                case MUL:
                    return leftSimpleExpression.mul(rightSimpleExpression);
                default:
                    return leftSimpleExpression.dif(rightSimpleExpression);
            }
        }
        int elemCheckResult = expression instanceof ElementExpression ? 1 : 0;
        List<Integer> params = new ArrayList<>();
        params.add(expression instanceof ConstExpression ? ((ConstExpression) expression).getNumber() : 0);
        if (elemCheckResult == 1) {
            params.add(1);
        }
        return new PolynomialExpression(elemCheckResult, params);
    }

    private static int isBooleanConstant(BinaryExpression expression) {
        if ((expression.getOperation() == Operation.EQUAL || expression.getOperation() == Operation.BETTER
                || expression.getOperation() == Operation.LESS)
                && ((PolynomialExpression) expression.getLeftOperand()).getMaxDegree() == 0) {
            if (expression.getOperation() == Operation.EQUAL) {
                if (((PolynomialExpression) expression.getLeftOperand()).getConstParameter() == 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
            if (expression.getOperation() == Operation.BETTER || expression.getOperation() == Operation.LESS) {
                PolynomialExpression leftOperand = ((PolynomialExpression) expression.getLeftOperand());
                if (expression.getOperation() == Operation.BETTER) {
                    return leftOperand.getConstParameter() > 0 ? 1 : 0;
                } else {
                    return leftOperand.getConstParameter() < 0 ? 1 : 0;
                }
            }
        }
        return -1;
    }

    private static Expression simplifyBooleanWithEqualsOperation(BinaryExpression leftExpression,
                                                                       BinaryExpression rightExpression,
                                                                       Operation global, Operation eq) {
        final Expression common = leftExpression.getLeftOperand();
        if (eq.isComparable()) {
            return leftExpression;
        }
        return new BinaryExpression(eq, common,
                fastAndOrSimplify(
                        (BinaryExpression) leftExpression.getRightOperand(),
                        (BinaryExpression) rightExpression.getRightOperand(),
                        global
                ));
    }

    private static Expression fastAndOrSimplify(BinaryExpression leftExpression,
                                                BinaryExpression rightExpression,
                                                Operation operation) {
        int isConstLeft = isBooleanConstant(leftExpression);
        int isConstRight = isBooleanConstant(rightExpression);
        if (isConstLeft == 1 && isConstRight == 1) {
            return constTrue;
        }
        if (isConstLeft == 1) {
            return operation == Operation.AND ? rightExpression : constTrue;
        }
        if (isConstRight == 1) {
            return operation == Operation.AND ? leftExpression : constTrue;
        }
        if (isConstLeft == 0 && isConstRight == 0) {
            return constFalse;
        }
        if (isConstLeft == 0) {
            return operation == Operation.AND ? constFalse : rightExpression;
        }
        if (isConstRight == 0) {
            return operation == Operation.AND ? constFalse : leftExpression;
        }
        if (leftExpression.getLeftOperand().equals(rightExpression.getLeftOperand())) {
            if (leftExpression.getOperation() == rightExpression.getOperation()) {
                return simplifyBooleanWithEqualsOperation(leftExpression, rightExpression, operation,
                        leftExpression.getOperation());
            }
            if (operation == Operation.AND && leftExpression.getOperation().isComparable()) {
                return constFalse;
            }
        }
        return new BinaryExpression(operation,
                leftExpression,
                rightExpression
        );
    }

    private static Expression andOrSimplify(BinaryExpression expression, Operation operation) {
        BinaryExpression leftExpression = (BinaryExpression)
                simplify(expression.getLeftOperand());
        BinaryExpression rightExpression = (BinaryExpression)
                simplify(expression.getRightOperand());
        return fastAndOrSimplify(leftExpression, rightExpression, operation);
    }

    /**
     * Try to simplify expression
     *
     * @param expression target to simplify
     * @return simplified expression equivalent incoming expression
     */
    public static Expression simplify(Expression expression) {
        if (expression instanceof BinaryExpression) {
            Operation operation = ((BinaryExpression) expression).getOperation();
            if (operation.isArithmetic()) {
                return arithmeticSimplify(expression);
            }
            if (operation.isComparable()) {
                PolynomialExpression leftExpression = arithmeticSimplify(
                            ((BinaryExpression) expression).getLeftOperand()
                        ).dif(arithmeticSimplify(((BinaryExpression) expression).getRightOperand()));
                return new BinaryExpression(operation, leftExpression, new ConstExpression(0));
            }
            return andOrSimplify((BinaryExpression) expression, operation);
        }
        return expression;
    }
}
