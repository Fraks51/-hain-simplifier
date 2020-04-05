package simplifier.exceptions;

public class TypeException extends Exception {
    /**
     * Type exception
     *
     * @param message exception's message
     */
    public TypeException(final String message) {
        super("TYPE_EXCEPTION:" + System.lineSeparator() + message);
    }
}
