import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import dto.MessageDTO;
import dto.UserDTO;
import injector.MyInitializer;
import injector.MyInjector;
import model.Message;
import my_exceptions.MessageNotExistsException;
import my_exceptions.TokenNotExistsException;
import my_exceptions.UserNotExistsException;
import my_exceptions.WrongPasswordException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.JVM)
public class MessageTest {

    Controller controller;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2-eclipselink"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        controller = injector.getInstance(Controller.class);
    }


    @Test
    public void sendMessageSuccess() throws UserNotExistsException, TokenNotExistsException, WrongPasswordException, MessageNotExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setToUser("Cristi");
        messageDTO.setContent("Hello");

        controller.sendMessage(gson.toJson(messageDTO), controller.login(gson.toJson(userDTO, UserDTO.class)));
    }

    @Test(expected = UserNotExistsException.class)
    public void sendMessageFail1() throws UserNotExistsException, TokenNotExistsException, WrongPasswordException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setToUser("B");
        messageDTO.setContent("Hello");

        controller.sendMessage(gson.toJson(messageDTO), controller.login(gson.toJson(userDTO, UserDTO.class)));
    }

    @Test(expected = WrongPasswordException.class)
    public void sendMessageFail2() throws UserNotExistsException, TokenNotExistsException, WrongPasswordException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "12");
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setToUser("B");
        messageDTO.setContent("Hello");

        controller.sendMessage(gson.toJson(messageDTO), controller.login(gson.toJson(userDTO, UserDTO.class)));
    }

    @Test(expected = TokenNotExistsException.class)
    public void sendMessageFail3() throws UserNotExistsException, TokenNotExistsException {
        Gson gson = new Gson();

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setToUser("Cristi");
        messageDTO.setContent("Hello");

        controller.sendMessage(gson.toJson(messageDTO), 999);
    }

    @Test
    public void getMessagesSuccess() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("Cristi", "Cristi", "1234");

        List<Message> messages = controller.getMessages(controller.login(gson.toJson(userDTO, UserDTO.class)));

        assertEquals("Cristi", messages.get(0).getToUser().getUsername());
        assertEquals("Hello", messages.get(0).getContent());
    }

    @Test(expected = UserNotExistsException.class)
    public void getMessagesFail1() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("C", "C", "1234");

        List<Message> messages = controller.getMessages(controller.login(gson.toJson(userDTO, UserDTO.class)));
    }

    @Test(expected = WrongPasswordException.class)
    public void getMessagesFail2() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("Cristi", "Cristi", "12");

        List<Message> messages = controller.getMessages(controller.login(gson.toJson(userDTO, UserDTO.class)));

        assertEquals("Cristi", messages.get(0).getToUser().getUsername());
        assertEquals("Hello", messages.get(0).getContent());
    }

    @Test(expected = TokenNotExistsException.class)
    public void getMessagesFail3() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        List<Message> messages = controller.getMessages(999);

        assertEquals("Cristi", messages.get(0).getToUser().getUsername());
        assertEquals("Hello", messages.get(0).getContent());
    }

    @Test
    public void getMessageByIdSuccess() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, MessageNotExistsException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("Cristi", "Cristi", "1234");

        List<Message> messages = controller.getMessages(controller.login(gson.toJson(userDTO, UserDTO.class)));

        Message message = controller.getMessageById(messages.get(0).getId(), controller.login(gson.toJson(userDTO, UserDTO.class)));

        assertEquals(messages.get(0), message);
    }

    @Test(expected = UserNotExistsException.class)
    public void getMessageByIdFail1() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, MessageNotExistsException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("AA", "AA", "1234");

        List<Message> messages = controller.getMessages(controller.login(gson.toJson(userDTO, UserDTO.class)));
    }

    @Test(expected = WrongPasswordException.class)
    public void getMessageByIdFail2() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, MessageNotExistsException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("Cristi", "Cristi", "12");

        List<Message> messages = controller.getMessages(controller.login(gson.toJson(userDTO, UserDTO.class)));

        Message message = controller.getMessageById(messages.get(0).getId(), controller.login(gson.toJson(userDTO, UserDTO.class)));

        assertEquals(messages.get(0), message);
    }

    @Test(expected = TokenNotExistsException.class)
    public void getMessageByIdFail3() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, MessageNotExistsException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("Cristi", "Cristi", "1234");

        List<Message> messages = controller.getMessages(controller.login(gson.toJson(userDTO, UserDTO.class)));

        Message message = controller.getMessageById(messages.get(0).getId(), 99);

        assertEquals(messages.get(0), message);
    }

    @Test(expected = MessageNotExistsException.class)
    public void getMessageByIdFail4() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, MessageNotExistsException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("Cristi", "Cristi", "1234");

        List<Message> messages = controller.getMessages(controller.login(gson.toJson(userDTO, UserDTO.class)));

        Message message = controller.getMessageById(99, controller.login(gson.toJson(userDTO, UserDTO.class)));

        assertEquals(messages.get(0), message);
    }

}
