package web_service;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import injector.MyInitializer;
import injector.MyInjector;
import my_exceptions.NullJsonContentException;
import my_exceptions.UserExistsException;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/register")
public class Register {

    Controller controller;

    public Register() {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        controller = injector.getInstance(Controller.class);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String register(String json) {
        Gson gson = new Gson();
        try {
            controller.register(json);
            return gson.toJson("The user has been registered");
        } catch (UserExistsException ex) {
            return gson.toJson("Register fails");
        } catch (NullJsonContentException e) {
            return gson.toJson("The json doesn't contains the expected information");
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
