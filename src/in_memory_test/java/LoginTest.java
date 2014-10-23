import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import dto.UserDTO;
import injector.MyInitializer;
import injector.MyInjector;
import my_exceptions.UserNotExistsException;
import my_exceptions.WrongPasswordException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.JVM)
public class LoginTest {

    Controller controller;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2-eclipselink"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        controller = injector.getInstance(Controller.class);
    }

    /**
     * Login successful.
     */

    @Test
    public void loginSuccess() throws UserNotExistsException, WrongPasswordException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        controller.login(gson.toJson(userDTO));
    }

    /**
     * The user doesn't exist
     */

    @Test(expected = UserNotExistsException.class)
    public void loginFai11() throws UserNotExistsException, WrongPasswordException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("C", "C", "1234");

        controller.login(gson.toJson(userDTO));
    }

    /**
     * The password it's wrong.
     */

    @Test(expected = WrongPasswordException.class)
    public void loginFail2() throws UserNotExistsException, WrongPasswordException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "12");

        controller.login(gson.toJson(userDTO));
    }

    /**
     * The token it's inserted correctly
     */

}
