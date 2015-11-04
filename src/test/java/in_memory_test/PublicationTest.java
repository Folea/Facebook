package in_memory_test;

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
import model.Publication;
import my_exceptions.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PublicationTest {

    /**
     * Test if createPost creates a post and when retrieve is the same that the one that was inserted.
     *
     * @throws UserNotExistsException                      If the user which will create the post doesn't exist.
     * @throws WrongPasswordException                      If the user that want to login have insert a wrong password.
     * @throws TokenNotExistsException                     If the token of the logged user is wrong.
     * @throws my_exceptions.PublicationNotExistsException If when try to retrieve the post that was inserted, the post doesn't exist in the DB.
     * @throws UserExistsException                         If the user to register doesn't exist.
     */

    @Test
    public void createPostSuccess() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, PublicationNotExistsException, UserExistsException, NullJsonContentException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);

        Gson gson = new Gson();

        PostDTO postDTO = new PostDTO();
        postDTO.setContent("Hello");
        UserDTO userDTO = new UserDTO("L", "L", "1234");

        controller.register(gson.toJson(userDTO, UserDTO.class));

        int id = controller.createPost(gson.toJson(postDTO), controller.login(gson.toJson(userDTO)));
        Publication post = controller.getPostByIdAndUser(id, controller.login(gson.toJson(userDTO, UserDTO.class)));

        assertEquals(id, post.getId());
        assertEquals(postDTO.getContent(), post.getContent());
    }

    /**
     * Test if getPosts retrieve a valid list of posts or an empty list.
     *
     * @throws UserNotExistsException  If the user which will create the post doesn't exist.
     * @throws WrongPasswordException  If the user that want to login have insert a wrong password.
     * @throws TokenNotExistsException If the token of the logged user is wrong.
     * @throws UserExistsException     If the user to register doesn't exist.
     */

    @Test
    public void getPostsSuccess() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, UserExistsException, NullJsonContentException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);

        Gson gson = new Gson();

        PostDTO postDTO = new PostDTO();
        postDTO.setContent("Hello");
        UserDTO userDTO = new UserDTO("M", "M", "1234");

        controller.register(gson.toJson(userDTO, UserDTO.class));

        int id = controller.createPost(gson.toJson(postDTO), controller.login(gson.toJson(userDTO)));

        List<Publication> posts = controller.getPosts(controller.login(gson.toJson(userDTO, UserDTO.class)));

        assertEquals(id, posts.get(0).getId());
        assertEquals(postDTO.getContent(), posts.get(0).getContent());
    }

    /**
     * Test if when try to get a post that doesn't exist in the DB will receive a PublicationNotExistException
     *
     * @throws UserNotExistsException                      If the user which will create the post doesn't exist.
     * @throws WrongPasswordException                      If the user that want to login have insert a wrong password.
     * @throws TokenNotExistsException                     If the token of the logged user is wrong.
     * @throws my_exceptions.PublicationNotExistsException If when try to retrieve the post that was inserted, the post doesn't exist in the DB.
     * @throws UserExistsException                         If the user to register doesn't exist.
     */

    @Test(expected = PublicationNotExistsException.class)
    public void getPostByIdAndUserFail2() throws UserNotExistsException, WrongPasswordException, TokenNotExistsException, PublicationNotExistsException, UserExistsException, NullJsonContentException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);

        Gson gson = new Gson();

        PostDTO postDTO = new PostDTO();
        postDTO.setContent("Hello");
        UserDTO userDTO = new UserDTO("O", "O", "1234");

        controller.register(gson.toJson(userDTO, UserDTO.class));

        int id = controller.createPost(gson.toJson(postDTO), controller.login(gson.toJson(userDTO)));

        Publication post = controller.getPostByIdAndUser(999, controller.login(gson.toJson(userDTO, UserDTO.class)));
    }

    /**
     * Test if when insert a comment the comment is inserted correctly. This test will test the getComments to by
     * retrieve the comments and compare it with the inserted one
     *
     * @throws UserExistsException
     * @throws UserNotExistsException
     * @throws WrongPasswordException
     * @throws my_exceptions.PublicationNotExistsException
     * @throws TokenNotExistsException
     */

    @Test
    public void commentPostSuccess() throws UserExistsException, UserNotExistsException, WrongPasswordException, PublicationNotExistsException, TokenNotExistsException, NullJsonContentException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("h2"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);

        Gson gson = new Gson();

        PostDTO postDTO = new PostDTO();
        postDTO.setContent("Hello");
        UserDTO userDTO = new UserDTO("LL", "LL", "1234");

        controller.register(gson.toJson(userDTO, UserDTO.class));

        int idPost = controller.createPost(gson.toJson(postDTO), controller.login(gson.toJson(userDTO)));
        Publication post = controller.getPostByIdAndUser(idPost, controller.login(gson.toJson(userDTO, UserDTO.class)));

        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setPost(idPost);
        publicationDTO.setContent("Comment");

        int idComment = controller.commentPost(gson.toJson(publicationDTO, PublicationDTO.class), controller.login(gson.toJson(userDTO)));

        List<Publication> comments = controller.getComments(post);

        assertEquals(post, ((Comment) comments.get(0)).getPublication());
    }
}
