package my_exceptions;

/**
 * Created by cristian.folea on 16.10.2014.
 */
public class UserNotExistsException extends Exception {
    public UserNotExistsException(Exception ex) {
        super(ex);
    }
}
