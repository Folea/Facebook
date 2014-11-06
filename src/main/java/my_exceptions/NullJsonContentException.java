package my_exceptions;

public class NullJsonContentException extends Exception{

    public NullJsonContentException() {
    }

    public NullJsonContentException(Exception ex) {
        super(ex);
    }

}
