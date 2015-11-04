package web_service;

import com.google.gson.Gson;
import controller.Controller;
import dto.UserDTO;
import my_exceptions.NullJsonContentException;
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
    public void registerSuccess() throws UserExistsException, NullJsonContentException {
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
    public void registerFail1() throws UserExistsException, NullJsonContentException {
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

    @Test
    public void registerFail2() throws UserExistsException, NullJsonContentException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO();
        userDTO.setName("Folea");
        userDTO.setUsername("Folea");
        userDTO.setPassword("1234");

        controller.register(isA(String.class));
        expectLastCall().andThrow(new NullJsonContentException());
        replay(controller);

        register.setController(controller);

        assertEquals(gson.toJson("The json doesn't contains the expected information"), register.register(gson.toJson(userDTO, UserDTO.class)));
    }

    @Test
    public void registerFail3() throws UserExistsException, NullJsonContentException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO();
        userDTO.setName("Folea");
        userDTO.setUsername("Folea");
        userDTO.setPassword("1234");

        controller.register("");
        controller.register(null);
        expectLastCall().andThrow(new NullJsonContentException());
        replay(controller);

        register.setController(controller);

        assertEquals(gson.toJson("The json doesn't contains the expected information"), register.register(null));
    }

    @Test
    public void registerFail4() throws UserExistsException, NullJsonContentException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO();
        userDTO.setName("Folea");
        userDTO.setUsername("Folea");
        userDTO.setPassword("1234");

        controller.register("");
        expectLastCall().andThrow(new NullJsonContentException());
        replay(controller);

        register.setController(controller);

        assertEquals(gson.toJson("The json doesn't contains the expected information"), register.register(""));
    }

}
