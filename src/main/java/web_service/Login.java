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
import my_exceptions.UserNotExistsException;
import my_exceptions.WrongPasswordException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/login")
public class Login {

    Controller controller;

    public Login() {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        controller = injector.getInstance(Controller.class);
    }

    /**
     * Login into the application. The login information will be passed by a json file.
     * @param json The json which contains the login information.
     * @return A json with the token id if the login was successful or will return a json containing information
     * about the exception that occurs when try to login.
     */

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String login(String json) {
        Gson gson = new Gson();
        try {
            ReturnDTO returnDTO = new ReturnDTO(controller.login(json), "User logged successful");
            return gson.toJson(returnDTO, ReturnDTO.class);
        } catch (UserNotExistsException ex) {
            return gson.toJson("The user doesn't exist");
        } catch (WrongPasswordException ex) {
            return gson.toJson("The password is wrong");
        } catch (NullJsonContentException e) {
            return gson.toJson("The json doesn't contains the expected information");
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
