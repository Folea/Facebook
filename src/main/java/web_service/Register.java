package web_service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import my_exceptions.UserExistsException;
import injector.MyInitializer;
import injector.MyInjector;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/register")
public class Register {

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String register(String json){
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        try{
            controller.register(json);
            return "The user has been registered";
        } catch (UserExistsException ex){
            return "Register fails";
        }
    }
}
