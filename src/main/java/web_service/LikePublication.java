package web_service;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import injector.MyInitializer;
import injector.MyInjector;
import my_exceptions.PublicationNotExistException;
import my_exceptions.TokenNotExistsException;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/like")
public class LikePublication {

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String likePost(String json) {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        Gson gson = new Gson();
        try {
            controller.likePublication(json);
            return gson.toJson("Like");
        } catch (TokenNotExistsException ex) {
            return gson.toJson("You are not logged");
        } catch (PublicationNotExistException ex) {
            return gson.toJson("Publication not exist");
        }

    }

}
