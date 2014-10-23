import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import dto.UserDTO;
import injector.MyInitializer;
import injector.MyInjector;
import my_exceptions.UserExistsException;
import my_exceptions.UserNotExistsException;
import my_exceptions.WrongPasswordException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.JVM)
public class RegisterTest {

    Controller controller;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2-eclipselink"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        controller = injector.getInstance(Controller.class);
    }

    /**
     * Test for a new user.
     *
     * @throws UserExistsException Exception that throws if the user already exists.
     */

    @Test
    public void registerTest1() throws UserExistsException, UserNotExistsException, WrongPasswordException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");
        UserDTO userDTO1 = new UserDTO("Cristi", "Cristi", "1234");
        controller.register(gson.toJson(userDTO));
        controller.register(gson.toJson(userDTO1));
        controller.login(gson.toJson(userDTO));
    }

    /**
     * Register a user that already exist.
     *
     * @throws UserExistsException Exception that throws if the user already exists.
     */

    @Test(expected = UserExistsException.class)
    public void registerTest2() throws UserExistsException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        controller.register(gson.toJson(userDTO));
    }

}
