package my_exceptions;

/**
 * LikeAlreadyExistException it's a exception that represents a database error and it's thrown when a like to persist
 * already exist.
 */

public class LikeAlreadyExistsException extends Exception {

    public LikeAlreadyExistsException() {
    }

    public LikeAlreadyExistsException(Exception ex) {
        super(ex);
    }

}
