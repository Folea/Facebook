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
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.JVM)
public class TokenTest {

    Controller controller;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2-eclipselink"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        controller = injector.getInstance(Controller.class);
    }

    @Test
    public void getUserByTokenSuccess() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");
        User user = controller.getUserByToken(controller.login(gson.toJson(userDTO, UserDTO.class)));
        assertEquals(user.getUsername(), userDTO.getUsername());
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getPassword(), userDTO.getPassword());
    }

    @Test(expected = UserNotExistsException.class)
    public void getUserByTokenFail1() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("C", "C", "1234");
        User user = controller.getUserByToken(controller.login(gson.toJson(userDTO, UserDTO.class)));

        assertEquals(user.getUsername(), userDTO.getUsername());
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getPassword(), userDTO.getPassword());
    }

    @Test(expected = WrongPasswordException.class)
    public void getUserByTokenFail2() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("Folea", "Folea", "12");
        User user = controller.getUserByToken(controller.login(gson.toJson(userDTO, UserDTO.class)));

        assertEquals(user.getUsername(), userDTO.getUsername());
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getPassword(), userDTO.getPassword());
    }

    @Test(expected = TokenNotExistsException.class)
    public void getUserByTokenFail3() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, UserExistsException {
        Gson gson = new Gson();
        User user = controller.getUserByToken(9999);
    }
}
