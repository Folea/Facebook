package web_service;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import dto.MessageDTO;
import dto.ReturnDTO;
import injector.MyInitializer;
import injector.MyInjector;
import model.Message;
import my_exceptions.MessageNotExistsException;
import my_exceptions.TokenNotExistsException;
import my_exceptions.UserNotExistsException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;

@Path("/message")
public class MessageService {

    Controller controller;

    public MessageService() {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        controller = injector.getInstance(Controller.class);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllMessages(@QueryParam("token") int token) {
        Gson gson = new Gson();
        try {
            List<Message> listMessages = controller.getMessages(token);
            List<MessageDTO> listMessagesDTO = new LinkedList<>();
            for (Message m : listMessages) {
                MessageDTO msg = new MessageDTO(m.getToUser().getUsername(), m.getFromUser().getUsername(), m.getContent());
                listMessagesDTO.add(msg);
            }
            return gson.toJson(listMessagesDTO, List.class);
        } catch (TokenNotExistsException ex) {
            return gson.toJson("Token is incorrect " + token);
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String sendMessage(String json, @QueryParam("token") int token) {
        Gson gson = new Gson();
        try {
            ReturnDTO returnDTO = new ReturnDTO(controller.sendMessage(json, token), "Message send");
            return gson.toJson(returnDTO, ReturnDTO.class);
        } catch (UserNotExistsException ex) {
            return gson.toJson("The user doesn't exist");
        } catch (TokenNotExistsException ex) {
            return gson.toJson("Must be logged");
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMessage(@PathParam("id") int id, @QueryParam("token") int token) {
        Gson gson = new Gson();
        try {
            Message message = controller.getMessageById(id, token);
            MessageDTO messageDTO = new MessageDTO(message.getToUser().getUsername(), message.getFromUser().getUsername(),
                    message.getContent());
            return gson.toJson(messageDTO, MessageDTO.class);
        } catch (MessageNotExistsException ex) {
            return gson.toJson("The message doesn't exist");
        } catch (TokenNotExistsException ex) {
            return gson.toJson("Must be logged");
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
