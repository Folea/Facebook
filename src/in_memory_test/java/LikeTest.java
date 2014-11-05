import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import dto.PostDTO;
import dto.PublicationDTO;
import dto.UserDTO;
import injector.MyInitializer;
import injector.MyInjector;
import model.Publication;
import my_exceptions.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LikeTest {

    /**
     * Tests if a like is persisted successful on the DB by insert the like and then retrieve it from the DB and compare
     * to the like that was inserted.
     *
     * @throws UserNotExistsException       If the user doesn't exist.
     * @throws WrongPasswordException       If the password of the user to login is wrong.
     * @throws TokenNotExistsException      If the token of the user to post the like is wrong.
     * @throws my_exceptions.PublicationNotExistsException If the publication to like doesn't exists.
     * @throws my_exceptions.LikeAlreadyExistsException    If the like already exists.
     * @throws UserExistsException          If the user to register already exists.
     */

    @Test
    public void likePublicationSuccess() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, PublicationNotExistsException, LikeAlreadyExistsException, UserExistsException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2-eclipselink"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);

        Gson gson = new Gson();

        PostDTO postDTO = new PostDTO();
        postDTO.setContent("Hello");
        UserDTO userDTO = new UserDTO("Q", "Q", "1234");

        controller.register(gson.toJson(userDTO, UserDTO.class));

        int idPost = controller.createPost(gson.toJson(postDTO), controller.login(gson.toJson(userDTO)));

        Publication post = controller.getPostByIdAndUser(idPost, controller.login(gson.toJson(userDTO, UserDTO.class)));

        PublicationDTO like = new PublicationDTO();
        like.setPost(idPost);

        int idLike = controller.likePublication(gson.toJson(like, PublicationDTO.class), controller.login(gson.toJson(userDTO, UserDTO.class)));

        assertEquals(post, controller.getLikesForPublication(post).get(0).getPublication());
        assertEquals(idLike, controller.getLikesForPublication(post).get(0).getId());
    }

    /**
     * Test if when try to insert a like that already exists will received a LikeAlreadyExistsException.
     *
     * @throws UserNotExistsException       If the user doesn't exist.
     * @throws WrongPasswordException       If the password of the user to login is wrong.
     * @throws TokenNotExistsException      If the token of the user to post the like is wrong.
     * @throws my_exceptions.PublicationNotExistsException If the publication to like doesn't exists.
     * @throws my_exceptions.LikeAlreadyExistsException    If the like already exists.
     * @throws UserExistsException          If the user to register already exists.
     */

    @Test(expected = LikeAlreadyExistsException.class)
    public void likePublicationFail5() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, PublicationNotExistsException, LikeAlreadyExistsException, UserExistsException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2-eclipselink"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);

        Gson gson = new Gson();

        PostDTO postDTO = new PostDTO();
        postDTO.setContent("Hello");
        UserDTO userDTO = new UserDTO("R", "R", "1234");

        controller.register(gson.toJson(userDTO, UserDTO.class));

        int idPost = controller.createPost(gson.toJson(postDTO), controller.login(gson.toJson(userDTO)));

        Publication post = controller.getPostByIdAndUser(idPost, controller.login(gson.toJson(userDTO, UserDTO.class)));

        PublicationDTO like = new PublicationDTO();
        like.setPost(idPost);

        controller.likePublication(gson.toJson(like, PublicationDTO.class), controller.login(gson.toJson(userDTO, UserDTO.class)));
        controller.likePublication(gson.toJson(like, PublicationDTO.class), controller.login(gson.toJson(userDTO, UserDTO.class)));
    }
}
