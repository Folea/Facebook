package in_memory_test;

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
import my_exceptions.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MessageTest {

    /**
     * Test the sendMessage.
     *
     * @throws UserNotExistsException    If the user to send doesn't exist.
     * @throws TokenNotExistsException   If the token of the logged user doesn't exist.
     * @throws WrongPasswordException    If the password of the login user is wrong.
     * @throws MessageNotExistsException If when try to retrieve the send message the message doesn't exist.
     * @throws UserExistsException       If the user to register already exists.
     */

    @Test
    public void sendMessageSuccess() throws UserNotExistsException, TokenNotExistsException, WrongPasswordException, MessageNotExistsException, UserExistsException, NullJsonContentException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);

        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("D", "D", "1234");
        UserDTO userDTO1 = new UserDTO("E", "E", "1234");

        controller.register(gson.toJson(userDTO, UserDTO.class));
        controller.register(gson.toJson(userDTO1, UserDTO.class));

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setToUser("E");
        messageDTO.setContent("Hello");

        controller.sendMessage(gson.toJson(messageDTO), controller.login(gson.toJson(userDTO1, UserDTO.class)));
    }

    /**
     * Test if getMessages retrieve a valid list of messages. If are no messages retrieve a empty list.
     *
     * @throws UserNotExistsException  If the user to send doesn't exist.
     * @throws TokenNotExistsException If the token of the logged user doesn't exist.
     * @throws WrongPasswordException  If the password of the login user is wrong.
     * @throws UserExistsException     If the user to register already exists.
     */

    @Test
    public void getMessagesSuccess() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, UserExistsException, NullJsonContentException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);

        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("F", "F", "1234");
        UserDTO userDTO1 = new UserDTO("G", "G", "1234");

        controller.register(gson.toJson(userDTO, UserDTO.class));
        controller.register(gson.toJson(userDTO1, UserDTO.class));

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setToUser("G");
        messageDTO.setContent("Hello");

        int id = controller.sendMessage(gson.toJson(messageDTO), controller.login(gson.toJson(userDTO, UserDTO.class)));

        List<Message> messages = controller.getMessages(controller.login(gson.toJson(userDTO1, UserDTO.class)));

        assertEquals(id, messages.get(0).getId());
        assertEquals(messageDTO.getContent(), messages.get(0).getContent());
        assertEquals("F", messages.get(0).getFromUser().getUsername());
    }

    /**
     * Test if the getMessageById it's retrieving the correct message from the database.
     *
     * @throws UserNotExistsException    If the user to send doesn't exist.
     * @throws TokenNotExistsException   If the token of the logged user doesn't exist.
     * @throws WrongPasswordException    If the password of the login user is wrong.
     * @throws MessageNotExistsException If when try to retrieve the send message the message doesn't exist.
     * @throws UserExistsException       If the user to register already exists.
     */

    @Test
    public void getMessageByIdSuccess() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, MessageNotExistsException, UserExistsException, NullJsonContentException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);

        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("H", "H", "1234");
        UserDTO userDTO1 = new UserDTO("I", "I", "1234");

        controller.register(gson.toJson(userDTO, UserDTO.class));
        controller.register(gson.toJson(userDTO1, UserDTO.class));

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setToUser("I");
        messageDTO.setContent("Hello");

        int id = controller.sendMessage(gson.toJson(messageDTO), controller.login(gson.toJson(userDTO, UserDTO.class)));

        Message message = controller.getMessageById(id, controller.login(gson.toJson(userDTO1, UserDTO.class)));

        assertEquals(id, message.getId());
    }

    /**
     * Test if when try to retrieve a message that doesn't exist will receive a MessageNotExistsException
     *
     * @throws UserNotExistsException    If the user to send doesn't exist.
     * @throws TokenNotExistsException   If the token of the logged user doesn't exist.
     * @throws WrongPasswordException    If the password of the login user is wrong.
     * @throws MessageNotExistsException If when try to retrieve the send message the message doesn't exist.
     * @throws UserExistsException       If the user to register already exists.
     */

    @Test(expected = MessageNotExistsException.class)
    public void getMessageByIdFail4() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, MessageNotExistsException, UserExistsException, NullJsonContentException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);

        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("J", "J", "1234");
        UserDTO userDTO1 = new UserDTO("K", "K", "1234");

        controller.register(gson.toJson(userDTO, UserDTO.class));
        controller.register(gson.toJson(userDTO1, UserDTO.class));

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setToUser("K");
        messageDTO.setContent("Hello");

        int id = controller.sendMessage(gson.toJson(messageDTO), controller.login(gson.toJson(userDTO, UserDTO.class)));

        Message message = controller.getMessageById(99, controller.login(gson.toJson(userDTO1, UserDTO.class)));
    }

}
