import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import my_exceptions.UserExistsException;
import injector.MyInitializer;
import injector.MyInjector;
import my_exceptions.UserNotExistsExcepiton;
import my_exceptions.WrongPasswordException;

public class Main {

    public static void main(String[] args0){
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        String json = "{\n" +
                "    \"name\" : \"Folea\",\n" +
                "    \"password\" : \"1234\"\n" +
                "}";
        try {
            controller.login(json);
        } catch (UserNotExistsExcepiton ex) {
            System.out.println( "The user doesn't exist");
        } catch (WrongPasswordException ex) {
            System.out.println("The password is wrong");
        }
    }

}
