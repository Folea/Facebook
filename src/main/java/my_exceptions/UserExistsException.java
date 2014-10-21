package my_exceptions;

/**
 * UserExistsException it's a exception that represents a database error and it's thrown when a user to persist
 * already exist.
 */
public class UserExistsException extends Exception {

    public UserExistsException() {

    }

    public UserExistsException(Exception ex) {
        super(ex);
    }
}
