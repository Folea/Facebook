package my_exceptions;

/**
 * PublicationNotExistException it's used to represent a database exception when a publication that is ask for doesn't exist.
 */

public class PublicationNotExistsException extends Exception {

    public PublicationNotExistsException() {

    }

    public PublicationNotExistsException(Exception ex) {
        super(ex);
    }
}
