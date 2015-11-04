package web_service;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import dto.ReturnDTO;
import injector.MyInitializer;
import injector.MyInjector;
import my_exceptions.NullJsonContentException;
import my_exceptions.PublicationNotExistsException;
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

    /**
     * commentPost receive a json and the token of the logged user and call controller commentPost which interprets the
     * json. If controller.commentPost can't create the comment will return a exception that identify the error.
     *
     * @param json  The json whit comments data.
     * @param token The token of the connected user.
     * @return Returns a Json. If the comment was created the json will contain information about the comment, otherwise
     * will return information about the problem.
     */

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
        } catch (PublicationNotExistsException ex) {
            return gson.toJson("Publication not exist");
        } catch (NullJsonContentException e) {
            return gson.toJson("The json doesn't contain the expected information");
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
