package my_exceptions;

/**
 * Created by cristian.folea on 16.10.2014.
 */
public class UserNotExistsExcepiton extends Exception {
    public UserNotExistsExcepiton(Exception ex){
        super(ex);
    }
}
