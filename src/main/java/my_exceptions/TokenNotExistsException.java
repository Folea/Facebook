package my_exceptions;

/**
 * TokenNotExistsException it's used to represent a database exception when a token that is ask for doesn't exist.
 */

public class TokenNotExistsException extends Exception {
    public TokenNotExistsException(Exception ex) {
        super(ex);
    }
}
