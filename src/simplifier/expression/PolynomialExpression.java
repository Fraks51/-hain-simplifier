package simplifier.expression;

import simplifier.exceptions.TypeException;

import java.util.*;
import java.util.stream.Collectors;

public class PolynomialExpression extends Expression {
    private int maxDegree;
    private List<Integer> degreesParams;

    private Expression nDegreeElement(int n) {
        Expression currentExpression = new ElementExpression();
        for (int i = 1; i < n; i++) {
            currentExpression = new BinaryExpression(Operation.MUL, new ElementExpression(), currentExpression);
        }
        return currentExpression;
    }

    public PolynomialExpression(int n, List<Integer> degreesParams) {
        this.degreesParams = degreesParams;
        maxDegree = n;
        returnedType = Integer.TYPE;
    }

    public PolynomialExpression add(PolynomialExpression polynomialExpression) throws TypeException {
        int maximum = Math.max(maxDegree, polynomialExpression.maxDegree);
        int minimum = Math.min(maxDegree, polynomialExpression.maxDegree);
        List<Integer> newDegreesParams = new ArrayList<>(Collections.nCopies(maximum + 1, 0));
        for (int i = 0; i <= maximum; i++) {
            if (i <= minimum) {
                newDegreesParams.set(i, degreesParams.get(i) + polynomialExpression.degreesParams.get(i));
            } else {
                if (maxDegree > polynomialExpression.maxDegree) {
                    newDegreesParams.set(i, degreesParams.get(i));
                } else {
                    newDegreesParams.set(i, polynomialExpression.degreesParams.get(i));
                }
            }
        }
        return new PolynomialExpression(maximum, newDegreesParams);
    }

    public PolynomialExpression dif(PolynomialExpression polynomialExpression) throws TypeException {
        polynomialExpression.degreesParams = polynomialExpression.degreesParams.stream()
                .map(x -> -x).collect(Collectors.toList());
        return add(polynomialExpression);
    }

    public PolynomialExpression mul(PolynomialExpression polynomialExpression) throws TypeException {
        int newMaxDegree = maxDegree + polynomialExpression.maxDegree;
        List<Integer> newDegreesParams = new ArrayList<>(Collections.nCopies(newMaxDegree + 1, 0));
        for (int i = 0; i <= maxDegree; i++) {
            for (int j = 0; j <= polynomialExpression.maxDegree; j++) {
                newDegreesParams.set(i + j, newDegreesParams.get(i + j) +
                        polynomialExpression.degreesParams.get(j) * degreesParams.get(i));
            }
        }
        return new PolynomialExpression(newMaxDegree, newDegreesParams);
    }

    private Expression buildExpression() {
        Expression currentExpression = new ConstExpression(degreesParams.get(0));
        for (int i = 1; i <= maxDegree; i++) {
            int param = degreesParams.get(i);
            if (param != 0) {
                if (param > 0) {
                    currentExpression = new BinaryExpression(
                            Operation.SUM,
                            param == 1 ?
                                    nDegreeElement(i) :
                                    new BinaryExpression(
                                            Operation.MUL,
                                            new ConstExpression(param),
                                            nDegreeElement(i)
                                    ),
                            currentExpression
                    );
                } else {
                    param = -param;
                    currentExpression = new BinaryExpression(
                            Operation.DIF,
                            param == 1 ?
                                    nDegreeElement(i) :
                                    new BinaryExpression(
                                            Operation.MUL,
                                            new ConstExpression(param),
                                            nDegreeElement(i)
                                    ),
                            currentExpression
                    );
                }
            }
        }
        return currentExpression;
    }

    @Override
    public Expression apply(Expression expression) {
        return null;
    }

    @Override
    public String toString() {
        Expression currentExpression = buildExpression();
        return currentExpression.toString();
    }

    public int getMaxDegree() {
        return maxDegree;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolynomialExpression that = (PolynomialExpression) o;
        return maxDegree == that.maxDegree &&
                degreesParams.equals(that.degreesParams);
    }

    public int getConstParameter() {
        return degreesParams.get(0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxDegree, degreesParams);
    }
}
