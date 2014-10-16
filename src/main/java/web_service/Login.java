package web_service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import injector.MyInitializer;
import injector.MyInjector;
import my_exceptions.UserNotExistsExcepiton;
import my_exceptions.WrongPasswordException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/login")
public class Login {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String login(String json) {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        try {
            controller.login(json);
        } catch (UserNotExistsExcepiton ex) {
            return "The user doesn't exist";
        } catch (WrongPasswordException ex) {
            return "The password is wrong";
        }
        return "Login succesful";
    }
}
