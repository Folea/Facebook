package web_service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import injector.MyInitializer;
import injector.MyInjector;
import model.Message;
import my_exceptions.TokenNotExistsException;
import my_exceptions.UserNotExistsExcepiton;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/message")
public class MessageService {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllMessages(@QueryParam("token") int token){
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        try {
            List<Message> listMessages = controller.getMessages(token);
            String messages = "";
            for (Message m : listMessages) {
                messages += m.getId() + ". \n" + "From: " + m.getFromUser() + "\n" + "Content: " + m.getContent() + "\n";
            }
            return messages;
        } catch (TokenNotExistsException ex){
            return "Token is incorrect " + token;
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String sendMessage(String json){
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        try{
            controller.sendMessage(json);
            return "Message send";
        } catch (UserNotExistsExcepiton ex) {
            return "The user doesn't exist";
        } catch (TokenNotExistsException ex){
            return "Must be logged";
        }
    }

    /*@GET
    @Path("/{id}")
    public String getMessage(@PathParam("id") int id) {

    }*/
}
