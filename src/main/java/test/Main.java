package test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.sun.org.apache.xerces.internal.impl.PropertyManager;
import controller.Controller;
import injector.MyInitializer;
import injector.MyInjector;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Main {

    public static void main(String[] args0) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream("/home/cristian.folea/project/facebook/src/main/resources/log4j.properties"));
        PropertyConfigurator.configure(props);

        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        //controller.register("Folea", "Folea", "1234");
        System.out.println(controller.login("Folea", "1234"));
        System.out.println("User it's logged");
        //controller.createPost("FOLEA FOLEA FOLEA");
        //controller.commentPost("commentssssda", 2);
        //controller.likePublication(2);
    }
}
