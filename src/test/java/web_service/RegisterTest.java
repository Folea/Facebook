package web_service;

import com.google.gson.Gson;
import controller.Controller;
import dto.UserDTO;
import my_exceptions.UserExistsException;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class RegisterTest {

    Register register;
    Controller controller;

    @Before
    public void setUp() {
        controller = createNiceMock(Controller.class);
        register = new Register();
    }

    @Test
    public void registerSuccess() throws UserExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO();
        userDTO.setName("Folea");
        userDTO.setUsername("Folea");
        userDTO.setPassword("1234");

        controller.register(isA(String.class));
        replay(controller);

        register.setController(controller);

        assertEquals(gson.toJson("The user has been registered"), register.register(gson.toJson(userDTO, UserDTO.class)));
    }

    @Test
    public void registerFail() throws UserExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO();
        userDTO.setName("Folea");
        userDTO.setUsername("Folea");
        userDTO.setPassword("1234");

        controller.register(isA(String.class));
        expectLastCall().andThrow(new UserExistsException());
        replay(controller);

        register.setController(controller);

        assertEquals(gson.toJson("Register fails"), register.register(gson.toJson(userDTO, UserDTO.class)));
    }

}
