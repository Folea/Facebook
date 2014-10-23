import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import dto.PublicationDTO;
import dto.UserDTO;
import injector.MyInitializer;
import injector.MyInjector;
import model.Likes;
import model.Publication;
import my_exceptions.*;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@FixMethodOrder(MethodSorters.JVM)
public class LikeTest {
    Controller controller;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2-eclipselink"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        controller = injector.getInstance(Controller.class);
    }

    @Test
    public void likePublicationSuccess() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, PublicationNotExistException, LikeAlreadyExistException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        PublicationDTO like = new PublicationDTO();
        like.setPost(posts.get(0).getId());

        controller.likePublication(gson.toJson(like, PublicationDTO.class), controller.login(gson.toJson(userDTO, UserDTO.class)));
        assertNotEquals(0, controller.getLikesForPublication(posts.get(0)));
    }

    @Test(expected = UserNotExistsException.class)
    public void likePublicationFail1() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, PublicationNotExistException, LikeAlreadyExistException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("F", "F", "1234");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));
    }

    @Test(expected = WrongPasswordException.class)
    public void likePublicationFail2() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, PublicationNotExistException, LikeAlreadyExistException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "12");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));
    }

    @Test(expected = TokenNotExistsException.class)
    public void likePublicationFail3() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, PublicationNotExistException, LikeAlreadyExistException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        PublicationDTO like = new PublicationDTO();
        like.setPost(posts.get(0).getId());

        controller.likePublication(gson.toJson(like, PublicationDTO.class), 999);
        assertNotEquals(0, controller.getLikesForPublication(posts.get(0)));
    }

    @Test(expected = PublicationNotExistException.class)
    public void likePublicationFail4() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, PublicationNotExistException, LikeAlreadyExistException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        PublicationDTO like = new PublicationDTO();
        like.setPost(999);

        controller.likePublication(gson.toJson(like, PublicationDTO.class), controller.login(gson.toJson(userDTO, UserDTO.class)));
        assertNotEquals(0, controller.getLikesForPublication(posts.get(0)));
    }

    @Test(expected = LikeAlreadyExistException.class)
    public void likePublicationFail5() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, PublicationNotExistException, LikeAlreadyExistException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        PublicationDTO like = new PublicationDTO();
        like.setPost(posts.get(0).getId());

        controller.likePublication(gson.toJson(like, PublicationDTO.class), controller.login(gson.toJson(userDTO, UserDTO.class)));
        assertNotEquals(0, controller.getLikesForPublication(posts.get(0)));
    }

    @Test
    public void getLikesForPublicationSuccess() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        List<Likes> likes = controller.getLikesForPublication(posts.get(0));

        assertEquals(posts.get(0), likes.get(0).getPublication());
    }

    @Test(expected = UserNotExistsException.class)
    public void getLikesForPublicationFail1() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("F", "Folea", "1234");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        List<Likes> likes = controller.getLikesForPublication(posts.get(0));

        assertEquals(posts.get(0), likes.get(0).getPublication());
    }

    @Test(expected = WrongPasswordException.class)
    public void getLikesForPublicationFail2() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "1");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        List<Likes> likes = controller.getLikesForPublication(posts.get(0));

        assertEquals(posts.get(0), likes.get(0).getPublication());
    }

    @Test(expected = TokenNotExistsException.class)
    public void getLikesForPublicationFail3() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        List<Publication> posts = controller.getPosts(999);

        List<Likes> likes = controller.getLikesForPublication(posts.get(0));

        assertEquals(posts.get(0), likes.get(0).getPublication());
    }

}
