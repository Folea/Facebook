import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import dto.UserDTO;
import injector.MyInitializer;
import injector.MyInjector;
import my_exceptions.NullJsonContentException;
import my_exceptions.UserExistsException;
import my_exceptions.UserNotExistsException;
import my_exceptions.WrongPasswordException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.JVM)
public class RegisterTest {


    /**
     * Test for a new user.
     *
     * @throws UserExistsException Exception that throws if the user already exists.
     */

    @Test
    public void registerTestSuccess() throws UserExistsException, UserNotExistsException, WrongPasswordException, NullJsonContentException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2"));
        injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);

        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");
        controller.register(gson.toJson(userDTO));
        controller.login(gson.toJson(userDTO));
    }

    /**
     * Register a user that already exist.
     *
     * @throws UserExistsException Exception that throws if the user already exists.
     */

    @Test(expected = UserExistsException.class)
    public void registerTestFail1() throws UserExistsException, NullJsonContentException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2"));
        injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);

        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("Cristi", "Cristi", "1234");

        controller.register(gson.toJson(userDTO));
        controller.register(gson.toJson(userDTO));
    }

}
