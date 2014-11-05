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
import org.junit.Test;

public class LoginTest {

    /**
     * Test the login with an user that exists in the DB.
     *
     * @throws UserNotExistsException If the user doesn't exist.
     * @throws WrongPasswordException If the password is wrong.
     * @throws UserExistsException    If the User exist when try to register it.
     */

    @Test
    public void loginSuccess() throws UserNotExistsException, WrongPasswordException, UserExistsException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2-eclipselink"));
        injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);

        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("A", "A", "1234");

        controller.register(gson.toJson(userDTO, UserDTO.class));
        controller.login(gson.toJson(userDTO));
    }

    /**
     * Test the login with an user that not exist and waiting for UserNotExistsException.
     *
     * @throws UserNotExistsException If the user doesn't exist.
     * @throws WrongPasswordException If the password is wrong.
     * @throws UserExistsException    If the User exist when try to register it.
     */

    @Test(expected = UserNotExistsException.class)
    public void loginFai11() throws UserNotExistsException, WrongPasswordException, UserExistsException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2-eclipselink"));
        injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);

        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("B", "B", "1234");

        controller.login(gson.toJson(userDTO));
    }

    /**
     * Test the login with a wrong password.
     *
     * @throws UserNotExistsException If the user doesn't exist.
     * @throws WrongPasswordException If the password is wrong.
     * @throws UserExistsException    If the User exist when try to register it.
     */

    @Test(expected = WrongPasswordException.class)
    public void loginFail2() throws UserNotExistsException, WrongPasswordException, UserExistsException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2-eclipselink"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);

        Gson gson = new Gson();

        UserDTO userDTO1 = new UserDTO("AA", "AA", "1234");
        UserDTO userDTO = new UserDTO("AA", "AA", "12");

        controller.register(gson.toJson(userDTO1, UserDTO.class));
        controller.login(gson.toJson(userDTO));
    }
}
