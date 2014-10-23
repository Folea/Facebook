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
import model.Comment;
import model.Post;
import model.Publication;
import my_exceptions.PublicationNotExistException;
import my_exceptions.TokenNotExistsException;
import my_exceptions.UserNotExistsException;
import my_exceptions.WrongPasswordException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.JVM)
public class PublicationTest {
    Controller controller;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2-eclipselink"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        controller = injector.getInstance(Controller.class);
    }

    @Test
    public void createPostSuccess() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, PublicationNotExistException {
        Gson gson = new Gson();

        PostDTO postDTO = new PostDTO();
        postDTO.setContent("Hello");
        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        int id = controller.createPost(gson.toJson(postDTO), controller.login(gson.toJson(userDTO)));
        Publication post = controller.getPostByIdAndUser(id, controller.login(gson.toJson(userDTO, UserDTO.class)));

        assertEquals(id, post.getId());
    }

    @Test(expected = UserNotExistsException.class)
    public void createPostFail1() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();

        PostDTO postDTO = new PostDTO();
        postDTO.setContent("Hello");

        UserDTO userDTO = new UserDTO("F", "F", "1234");

        controller.createPost(gson.toJson(postDTO), controller.login(gson.toJson(userDTO)));
    }

    @Test(expected = WrongPasswordException.class)
    public void createPostFail2() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();

        PostDTO postDTO = new PostDTO();
        postDTO.setContent("Hello");
        UserDTO userDTO = new UserDTO("Folea", "Folea", "12");

        controller.createPost(gson.toJson(postDTO), controller.login(gson.toJson(userDTO)));
    }

    @Test(expected = TokenNotExistsException.class)
    public void createPostFail3() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();

        PostDTO postDTO = new PostDTO();
        postDTO.setContent("Hello");

        controller.createPost(gson.toJson(postDTO), 999);
    }

    @Test
    public void getPostsSuccess() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        assertEquals("Hello", posts.get(0).getContent());
        assertEquals("Folea", ((Post) posts.get(0)).getFromUser().getUsername());
    }

    @Test(expected = UserNotExistsException.class)
    public void getPostsFail1() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("F", "F", "1234");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));
    }

    @Test(expected = WrongPasswordException.class)
    public void getPostsFail2() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "12");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));
    }

    @Test(expected = TokenNotExistsException.class)
    public void getPostsFail3() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        List<Publication> posts = controller.getPosts(999);
    }

    @Test(expected = UserNotExistsException.class)
    public void getPostByIdAndUserFail() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, PublicationNotExistException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("F", "F", "1234");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        Publication post = controller.getPostByIdAndUser(posts.get(0).getId(), controller.login(gson.toJson(userDTO, UserDTO.class)));

        assertEquals(posts.get(0), post.getId());
    }

    @Test(expected = WrongPasswordException.class)
    public void getPostByIdAndUserFail2() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, PublicationNotExistException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "12");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        Publication post = controller.getPostByIdAndUser(posts.get(0).getId(), controller.login(gson.toJson(userDTO, UserDTO.class)));

        assertEquals(posts.get(0), post.getId());
    }

    @Test(expected = TokenNotExistsException.class)
    public void getPostByIdAndUserFail3() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, PublicationNotExistException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        Publication post = controller.getPostByIdAndUser(posts.get(0).getId(), 999);

        assertEquals(posts.get(0), post.getId());
    }

    @Test(expected = PublicationNotExistException.class)
    public void getPostByIdAndUserFail4() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, PublicationNotExistException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        Publication post = controller.getPostByIdAndUser(999, controller.login(gson.toJson(userDTO, UserDTO.class)));

        assertEquals(posts.get(0), post.getId());
    }

    @Test
    public void commentPostSuccess() throws UserNotExistsException, WrongPasswordException, PublicationNotExistException, TokenNotExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent("Comment");
        publicationDTO.setPost(posts.get(0).getId());

        controller.commentPost(gson.toJson(publicationDTO, PublicationDTO.class), controller.login(gson.toJson(userDTO, UserDTO.class)));

        List<Publication> comments = controller.getComments(posts.get(0));

        assertEquals("Comment", comments.get(0).getContent());
        assertEquals(publicationDTO.getPost(), ((Comment) comments.get(0)).getPublication().getId());
    }

    @Test(expected = UserNotExistsException.class)
    public void commentPostFail1() throws UserNotExistsException, WrongPasswordException, PublicationNotExistException, TokenNotExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("F", "F", "1234");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent("Comment");
        publicationDTO.setPost(posts.get(0).getId());

        controller.commentPost(gson.toJson(publicationDTO, PublicationDTO.class), controller.login(gson.toJson(userDTO, UserDTO.class)));
    }

    @Test(expected = WrongPasswordException.class)
    public void commentPostFail2() throws UserNotExistsException, WrongPasswordException, PublicationNotExistException, TokenNotExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "12");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent("Comment");
        publicationDTO.setPost(posts.get(0).getId());

        controller.commentPost(gson.toJson(publicationDTO, PublicationDTO.class), controller.login(gson.toJson(userDTO, UserDTO.class)));
    }

    @Test(expected = PublicationNotExistException.class)
    public void commentPostFail3() throws UserNotExistsException, WrongPasswordException, PublicationNotExistException, TokenNotExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent("Comment");
        publicationDTO.setPost(999);

        controller.commentPost(gson.toJson(publicationDTO, PublicationDTO.class), controller.login(gson.toJson(userDTO, UserDTO.class)));

    }

    @Test(expected = TokenNotExistsException.class)
    public void commentPostFail4() throws UserNotExistsException, WrongPasswordException, PublicationNotExistException, TokenNotExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent("Comment");
        publicationDTO.setPost(posts.get(0).getId());

        controller.commentPost(gson.toJson(publicationDTO, PublicationDTO.class), 999);
    }

    @Test
    public void getCommentsSuccess() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        List<Publication> comments = controller.getComments(posts.get(0));

        assertEquals(posts.get(0), ((Comment) comments.get(0)).getPublication());
    }

    @Test(expected = UserNotExistsException.class)
    public void getCommentsFail1() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("F", "F", "1234");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));
    }

    @Test(expected = WrongPasswordException.class)
    public void getCommentsFail2() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "12");

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));
    }

    @Test(expected = TokenNotExistsException.class)
    public void getCommentsFail3() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException {
        Gson gson = new Gson();

        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        List<Publication> posts = controller.getPosts(999);
    }
}
