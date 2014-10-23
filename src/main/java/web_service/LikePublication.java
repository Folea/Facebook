package web_service;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import dto.ReturnDTO;
import injector.MyInitializer;
import injector.MyInjector;
import my_exceptions.LikeAlreadyExistException;
import my_exceptions.PublicationNotExistException;
import my_exceptions.TokenNotExistsException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/like")
public class LikePublication {

    Controller controller;

    public LikePublication() {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        controller = injector.getInstance(Controller.class);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String likePost(String json, @QueryParam("token") int token) {
        Gson gson = new Gson();
        try {
            ReturnDTO returnDTO = new ReturnDTO(controller.likePublication(json, token), "Like");
            return gson.toJson(returnDTO, ReturnDTO.class);
        } catch (TokenNotExistsException ex) {
            return gson.toJson("You are not logged");
        } catch (PublicationNotExistException ex) {
            return gson.toJson("Publication not exist");
        } catch (LikeAlreadyExistException ex) {
            return gson.toJson("The like already exist");
        }

    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
