package simplifier.exceptions;

public class SyntaxException extends Exception {
    /**
     * Syntax exception
     *
     * @param message exception's message
     */
    public SyntaxException(final String message) {
        super("SYNTAX_EXCEPTION:" + System.lineSeparator() + message);
    }
}
