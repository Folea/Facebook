package web_service;

import com.google.gson.Gson;
import controller.Controller;
import dto.MessageDTO;
import dto.ReturnDTO;
import model.Message;
import model.User;
import my_exceptions.MessageNotExistsException;
import my_exceptions.NullJsonContentException;
import my_exceptions.TokenNotExistsException;
import my_exceptions.UserNotExistsException;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class MessageServiceTest {

    MessageService messageService;
    Controller controller;

    @Before
    public void setUp() {
        messageService = new MessageService();
        controller = createNiceMock(Controller.class);
    }

    @Test
    public void getAllMessagesSuccess() throws TokenNotExistsException {
        Gson gson = new Gson();
        User user = new User("Folea", "Folea", "1234");
        User user1 = new User("Cristi", "Cristi", "1234");

        Message message = new Message("Hello", user, user1);
        message.setId(5);
        List<Message> listMessages = new LinkedList<>();
        listMessages.add(message);

        expect(controller.getMessages(1)).andReturn(listMessages);
        expect(controller.getUserByToken(2)).andReturn(user);

        replay(controller);

        messageService.setController(controller);
        List<MessageDTO> listMessagesDTO = new LinkedList<>();
        MessageDTO msg = new MessageDTO("Cristi", "Folea", "Hello");
        listMessagesDTO.add(msg);

        assertEquals(gson.toJson(listMessagesDTO, List.class), messageService.getAllMessages(1));
    }

    @Test
    public void getAllMessagesFail() throws TokenNotExistsException {
        Gson gson = new Gson();
        User user = new User("Folea", "Folea", "1234");
        User user1 = new User("Cristi", "Cristi", "1234");

        Message message = new Message("Hello", user, user1);
        message.setId(5);
        List<Message> listMessages = new LinkedList<>();
        listMessages.add(message);
        expect(controller.getMessages(1)).andThrow(new TokenNotExistsException());

        replay(controller);

        messageService.setController(controller);
        List<MessageDTO> listMessagesDTO = new LinkedList<>();
        MessageDTO msg = new MessageDTO("Cristi", "Folea", "Hello");
        listMessagesDTO.add(msg);

        assertEquals(gson.toJson("Token is incorrect " + 1), messageService.getAllMessages(1));
    }

    @Test
    public void sendMessageSuccess() throws UserNotExistsException, TokenNotExistsException, NullJsonContentException {
        Gson gson = new Gson();

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent("Hello");
        messageDTO.setToUser("Folea");
        messageDTO.setFromUser("Folea");
        expect(controller.sendMessage(gson.toJson(messageDTO), 1)).andReturn(2);

        replay(controller);

        messageService.setController(controller);
        ReturnDTO returnDTO = gson.fromJson(messageService.sendMessage(gson.toJson(messageDTO), 1), ReturnDTO.class);
        assertEquals(2, returnDTO.getId());
    }

    @Test
    public void sendMessageFail1() throws UserNotExistsException, TokenNotExistsException, NullJsonContentException {
        Gson gson = new Gson();

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent("Hello");
        messageDTO.setToUser("Folea");
        messageDTO.setFromUser("Folea");
        expect(controller.sendMessage(gson.toJson(messageDTO), 1)).andThrow(new UserNotExistsException());

        replay(controller);

        messageService.setController(controller);
        assertEquals(gson.toJson("The user doesn't exist"), messageService.sendMessage(gson.toJson(messageDTO), 1));
    }

    @Test
    public void sendMessageFail2() throws UserNotExistsException, TokenNotExistsException, NullJsonContentException {
        Gson gson = new Gson();

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent("Hello");
        messageDTO.setToUser("Folea");
        messageDTO.setFromUser("Folea");
        expect(controller.sendMessage(gson.toJson(messageDTO), 1)).andThrow(new TokenNotExistsException());

        replay(controller);

        messageService.setController(controller);
        assertEquals(gson.toJson("Must be logged"), messageService.sendMessage(gson.toJson(messageDTO), 1));
    }

    @Test
    public void sendMessageFail3() throws UserNotExistsException, TokenNotExistsException, NullJsonContentException {
        Gson gson = new Gson();

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent("Hello");
        messageDTO.setToUser("Folea");
        messageDTO.setFromUser("Folea");
        expect(controller.sendMessage(gson.toJson(messageDTO), 1)).andThrow(new NullJsonContentException());

        replay(controller);

        messageService.setController(controller);
        assertEquals(gson.toJson("The json doesn't contains the expected information"), messageService.sendMessage(gson.toJson(messageDTO), 1));
    }

    @Test
    public void getMessageSuccess() throws TokenNotExistsException, MessageNotExistsException {
        Gson gson = new Gson();
        User user = new User("Folea", "Folea", "1234");
        User user1 = new User("Cristi", "Cristi", "1234");

        Message message = new Message("Hello", user, user1);
        MessageDTO messageDTO = new MessageDTO(message.getToUser().getUsername(), message.getFromUser().getUsername(),
                message.getContent());
        expect(controller.getMessageById(5, 1)).andReturn(message);

        replay(controller);

        messageService.setController(controller);
        assertEquals(gson.toJson(messageDTO), messageService.getMessage(5, 1));
    }

    @Test
    public void getMessageFail1() throws TokenNotExistsException, MessageNotExistsException {
        Gson gson = new Gson();
        User user = new User("Folea", "Folea", "1234");
        User user1 = new User("Cristi", "Cristi", "1234");

        Message message = new Message("Hello", user, user1);
        MessageDTO messageDTO = new MessageDTO(message.getToUser().getUsername(), message.getFromUser().getUsername(),
                message.getContent());
        expect(controller.getMessageById(5, 1)).andThrow(new TokenNotExistsException());

        replay(controller);

        messageService.setController(controller);
        assertEquals(gson.toJson("Must be logged"), messageService.getMessage(5, 1));
    }

    @Test
    public void getMessageFail2() throws TokenNotExistsException, MessageNotExistsException {
        Gson gson = new Gson();
        User user = new User("Folea", "Folea", "1234");
        User user1 = new User("Cristi", "Cristi", "1234");

        Message message = new Message("Hello", user, user1);
        MessageDTO messageDTO = new MessageDTO(message.getToUser().getUsername(), message.getFromUser().getUsername(),
                message.getContent());
        expect(controller.getMessageById(5, 1)).andThrow(new MessageNotExistsException());

        replay(controller);

        messageService.setController(controller);
        assertEquals(gson.toJson("The message doesn't exist"), messageService.getMessage(5, 1));
    }

}
