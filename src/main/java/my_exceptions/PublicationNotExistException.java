package my_exceptions;

/**
 * PublicationNotExistException it's used to represent a database exception when a publication that is ask for doesn't exist.
 */

public class PublicationNotExistException extends Exception {

    public PublicationNotExistException() {

    }

    public PublicationNotExistException(Exception ex) {
        super(ex);
    }
}
