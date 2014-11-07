import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import dto.UserDTO;
import injector.MyInitializer;
import injector.MyInjector;
import my_exceptions.*;


public class Main {

    public static void main(String[] args0) throws TokenNotExistsException, MessageNotExistsException, UserExistsException, UserNotExistsException, WrongPasswordException, PublicationNotExistsException, NullJsonContentException, LikeAlreadyExistsException {

        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("1234");
        userDTO.setUsername("Folea");
        userDTO.setName("Folea");

        //controller.register(gson.toJson(userDTO, UserDTO.class));
        //controller.login(gson.toJson(userDTO, UserDTO.class));


        /*PostDTO postDTO  = new PostDTO();
        postDTO.setContent("sssa");
        controller.createPost(gson.toJson(postDTO, PostDTO.class), 1);*/

        /*PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setPost(51);
        controller.likePublication(gson.toJson(publicationDTO), 1);*/

        /*PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent("test");
        publicationDTO.setPost(9999);
        controller.commentPost(gson.toJson(publicationDTO, PublicationDTO.class), 1);*/
    }

}
