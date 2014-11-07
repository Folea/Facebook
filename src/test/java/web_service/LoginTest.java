package web_service;

import com.google.gson.Gson;
import controller.Controller;
import dto.ReturnDTO;
import dto.UserDTO;
import my_exceptions.NullJsonContentException;
import my_exceptions.UserNotExistsException;
import my_exceptions.WrongPasswordException;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class LoginTest {

    Login login;
    Controller controller;

    @Before
    public void setUp() {
        controller = createNiceMock(Controller.class);
        login = new Login();
    }

    @Test
    public void loginSuccess() throws UserNotExistsException, WrongPasswordException, NullJsonContentException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Folea");
        userDTO.setPassword("1234");

        expect(controller.login(gson.toJson(userDTO))).andReturn(5);

        replay(controller);

        login.setController(controller);

        ReturnDTO returnDTO = gson.fromJson(login.login(gson.toJson(userDTO)), ReturnDTO.class);
        assertEquals(5, returnDTO.getId());
    }

    @Test
    public void loginFail1() throws UserNotExistsException, WrongPasswordException, NullJsonContentException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Folea");
        userDTO.setPassword("1234");

        expect(controller.login(gson.toJson(userDTO))).andThrow(new UserNotExistsException());

        replay(controller);

        login.setController(controller);

        assertEquals(gson.toJson("The user doesn't exist"), login.login(gson.toJson(userDTO)));
    }

    @Test
    public void loginFail2() throws UserNotExistsException, WrongPasswordException, NullJsonContentException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Folea");
        userDTO.setPassword("1234");

        expect(controller.login(gson.toJson(userDTO))).andThrow(new WrongPasswordException());

        replay(controller);

        login.setController(controller);

        assertEquals(gson.toJson("The password is wrong"), login.login(gson.toJson(userDTO)));
    }

    @Test
    public void loginFail3() throws UserNotExistsException, WrongPasswordException, NullJsonContentException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Folea");
        userDTO.setPassword("1234");

        expect(controller.login(gson.toJson(userDTO))).andThrow(new NullJsonContentException());

        replay(controller);

        login.setController(controller);

        assertEquals(gson.toJson("The json doesn't contains the expected information"), login.login(gson.toJson(userDTO)));
    }

    @Test
    public void loginFail4() throws UserNotExistsException, WrongPasswordException, NullJsonContentException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Folea");
        userDTO.setPassword("1234");

        expect(controller.login(null)).andThrow(new NullJsonContentException());
        replay(controller);

        login.setController(controller);

        assertEquals(gson.toJson("The json doesn't contains the expected information"), login.login(null));
    }

    @Test
    public void loginFail5() throws UserNotExistsException, WrongPasswordException, NullJsonContentException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Folea");
        userDTO.setPassword("1234");

        expect(controller.login("")).andThrow(new NullJsonContentException());
        replay(controller);

        login.setController(controller);

        assertEquals(gson.toJson("The json doesn't contains the expected information"), login.login(""));
    }
}
