package my_exceptions;

public class PublicationNotExistException extends Exception {

    public PublicationNotExistException(Exception ex) {
        super(ex);
    }
}
