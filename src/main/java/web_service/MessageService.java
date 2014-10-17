package web_service;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import dto.MessageDTO;
import injector.MyInitializer;
import injector.MyInjector;
import model.Message;
import my_exceptions.TokenNotExistsException;
import my_exceptions.UserNotExistsException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;

@Path("/message")
public class MessageService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllMessages(@QueryParam("token") int token) {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        Gson gson = new Gson();
        try {
            List<Message> listMessages = controller.getMessages(token);
            List<MessageDTO> listMessagesDTO = new LinkedList<>();
            for (Message m : listMessages) {
                MessageDTO msg = new MessageDTO(m.getToUser().getUsername(), controller.getUserByToken(token).getUsername(), m.getContent());
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
    public String sendMessage(String json) {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        Gson gson = new Gson();
        try {
            controller.sendMessage(json);
            return gson.toJson("Message send");
        } catch (UserNotExistsException ex) {
            return gson.toJson("The user doesn't exist");
        } catch (TokenNotExistsException ex) {
            return gson.toJson("Must be logged");
        }
    }

    /*@GET
    @Path("/{id}")
    public String getMessage(@PathParam("id") int id) {

    }*/
}
