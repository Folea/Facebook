package my_exceptions;

/**
 * LikeAlreadyExistException it's a exception that represents a database error and it's thrown when a like to persist
 * already exist.
 */

public class LikeAlreadyExistException extends Exception {

    public LikeAlreadyExistException(Exception ex) {
        super(ex);
    }

}
