import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import injector.MyInitializer;
import injector.MyInjector;
import my_exceptions.TokenNotExistsException;


public class Main {

    public static void main(String[] args0) throws TokenNotExistsException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        Gson gson = new Gson();
        //PublicationDTO pub = new PublicationDTO("aaaaa", Integer.toString(2), Integer.toString(2));
        //System.out.println(pub.getContent() + "     " + pub.getPost());
    }

}
