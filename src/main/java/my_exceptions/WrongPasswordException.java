package my_exceptions;

/**
 * WrongPasswordException it's a exception that it's thrown when the password that a user introduce to login and the
 * persisted user password doesn't match.
 */

public class WrongPasswordException extends Exception {

    public WrongPasswordException() {
    }

    public WrongPasswordException(Exception ex) {
        super(ex);
    }
}
