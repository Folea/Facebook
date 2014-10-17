import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import my_exceptions.TokenNotExistsException;
import my_exceptions.UserExistsException;
import injector.MyInitializer;
import injector.MyInjector;
import my_exceptions.UserNotExistsExcepiton;
import my_exceptions.WrongPasswordException;

public class Main {

    public static void main(String[] args0) throws TokenNotExistsException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        String json = "{\n" +
                "    \"username\" : \"Cristi\",\n" +
                "    \"content\" : \"Salut\"\n" +
                "}";
            //System.out.println("hello \n" + controller.getMessages(701).size());
        System.out.println(controller.getPosts(651).size());
    }


}
