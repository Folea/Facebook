package my_exceptions;

/**
 * Created by cristian.folea on 16.10.2014.
 */
public class TokenNotExistsException extends Exception {
    public TokenNotExistsException(Exception ex) {
        super(ex);
    }
}
