package web_service;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import dto.ReturnDTO;
import injector.MyInitializer;
import injector.MyInjector;
import my_exceptions.PublicationNotExistException;
import my_exceptions.TokenNotExistsException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/comment")
public class CommentPost {

    Controller controller;

    public CommentPost() {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        controller = injector.getInstance(Controller.class);
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String commentPost(String json, @QueryParam("token") int token) {
        Gson gson = new Gson();
        try {
            ReturnDTO returnDTO = new ReturnDTO(controller.commentPost(json, token), "Commented");
            return gson.toJson(returnDTO, ReturnDTO.class);
        } catch (TokenNotExistsException ex) {
            return gson.toJson("Comment fail");
        } catch (PublicationNotExistException ex) {
            return gson.toJson("Publication not exist");
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
