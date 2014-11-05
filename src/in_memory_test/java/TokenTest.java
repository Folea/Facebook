import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import dto.UserDTO;
import injector.MyInitializer;
import injector.MyInjector;
import model.User;
import my_exceptions.TokenNotExistsException;
import my_exceptions.UserExistsException;
import my_exceptions.UserNotExistsException;
import my_exceptions.WrongPasswordException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.JVM)
public class TokenTest {

    /**
     * This test will test if getUserByToken will retrieve the correct user.
     *
     * @throws UserNotExistsException  If the user to login doesn't exist.
     * @throws WrongPasswordException  If when try to login introduce a wrong password.
     * @throws TokenNotExistsException If the token of the user to retrieve doesn't exist.
     * @throws UserExistsException     If the user to register already exists.
     */

    @Test
    public void getUserByTokenSuccess() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, UserExistsException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2-eclipselink"));
        injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);

        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("C", "C", "1234");

        controller.register(gson.toJson(userDTO, UserDTO.class));

        User user = controller.getUserByToken(controller.login(gson.toJson(userDTO, UserDTO.class)));
        assertEquals(user.getUsername(), userDTO.getUsername());
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getPassword(), userDTO.getPassword());
    }

    /**
     * This test will test what happens when try to retrieve a user by a token that doesn't exist. It will wait a
     * TokenNotExistsException.
     *
     * @throws UserNotExistsException  If the user to login doesn't exist.
     * @throws WrongPasswordException  If when try to login introduce a wrong password.
     * @throws TokenNotExistsException If the token of the user to retrieve doesn't exist.
     * @throws UserExistsException     If the user to register already exists.
     */

    @Test(expected = TokenNotExistsException.class)
    public void getUserByTokenFail3() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, UserExistsException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2-eclipselink"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        Gson gson = new Gson();
        User user = controller.getUserByToken(9999);
    }
}
