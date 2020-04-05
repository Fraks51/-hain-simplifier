package simplifier.expression;

import simplifier.exceptions.SyntaxException;

import java.lang.reflect.Type;

public enum Operation {
    BETTER(">", Boolean.TYPE, Integer.TYPE),
    LESS("<", Boolean.TYPE, Integer.TYPE),
    EQUAL("=", Boolean.TYPE, Integer.TYPE),
    OR("|", Boolean.TYPE, Boolean.TYPE),
    AND("&", Boolean.TYPE, Boolean.TYPE),
    SUM("+", Integer.TYPE, Integer.TYPE),
    DIF("-", Integer.TYPE, Integer.TYPE),
    MUL("*", Integer.TYPE, Integer.TYPE);

    private final String stringFormat;
    private final Type returnedType;
    private final Type operandsType;

    Operation(final String stringFormat, final Type returnedType, final Type operandsType) {
        this.stringFormat = stringFormat;
        this.returnedType = returnedType;
        this.operandsType = operandsType;
    }

    /**
     * Get {@link Operation} appropriate given char
     * @param c target to find {@link Operation}
     * @return operation by char
     * @throws SyntaxException if couldn't find operation by char
     */
    public static Operation fromChar(final char c) throws SyntaxException {
        switch(c) {
            case '>': return BETTER;
            case '<': return LESS;
            case '=': return EQUAL;
            case '|': return OR;
            case '&': return AND;
            case '+': return SUM;
            case '-': return DIF;
            case '*': return MUL;
        }
        throw new SyntaxException("Unknown operation: " + c);
    }

    public Type getReturnedType() {
        return returnedType;
    }

    public Type getOperandsType() {
        return operandsType;
    }

    public String toString() {
        return stringFormat;
    }
}
