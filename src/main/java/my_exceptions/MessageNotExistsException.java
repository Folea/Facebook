package my_exceptions;

/**
 * MessageNotExistsException it's used to represent a database exception when a message that is ask for doesn't exist.
 */

public class MessageNotExistsException extends Exception {

    public MessageNotExistsException() {

    }

    public MessageNotExistsException(Exception ex) {
        super(ex);
    }
}
