package my_exceptions;

/**
 * Created by cristian.folea on 16.10.2014.
 */
public class UserExistsException extends Exception {
    public UserExistsException(Exception ex){
        super(ex);
    }
}
