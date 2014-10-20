package my_exceptions;

/**
 * UserNotExistsException it's used to represent a database exception when a user that is ask for doesn't exist.
 */

public class UserNotExistsException extends Exception {
    public UserNotExistsException(Exception ex) {
        super(ex);
    }
}
