package controller;

import com.google.gson.Gson;
import dao.*;
import dto.MessageDTO;
import dto.PublicationDTO;
import dto.UserDTO;
import model.*;
import my_exceptions.*;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.RollbackException;
import java.util.LinkedList;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class ControllerTest {

    private Controller controller;
    private LikesDAO likes;
    private MessageDAO messages;
    private PublicationDAO publications;
    private TokenDAO tokens;
    private UserDAO users;

    @Before
    public void setUp() throws TokenNotExistsException {
        likes = createNiceMock(LikesDAO.class);
        messages = createNiceMock(MessageDAO.class);
        publications = createNiceMock(PublicationDAO.class);
        tokens = createNiceMock(TokenDAO.class);
        users = createNiceMock(UserDAO.class);
        controller = new Controller(likes, messages, publications, users, tokens);
    }

    @Test
    public void getUserByTokenTestSuccess() throws TokenNotExistsException {
        expect(tokens.getTokenById(50)).andReturn((new Token(new User("Cristi", "Cristi", "1234"))));
        replay(tokens);

        assertEquals(new User("Cristi", "Cristi", "1234"), controller.getUserByToken(50));

    }

    @Test(expected = TokenNotExistsException.class)
    public void getUserByTokenTestFail() throws TokenNotExistsException {
        expect(tokens.getTokenById(55)).andThrow(new TokenNotExistsException());
        replay(tokens);

        controller.getUserByToken(55);
    }

    @Test
    public void registerTestSuccess() throws UserExistsException, NullJsonContentException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        controller.register(gson.toJson(userDTO));
    }

    @Test(expected = UserExistsException.class)
    public void registerTestFail() throws UserExistsException, NullJsonContentException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        users.insert(isA(User.class));
        expectLastCall().andThrow(new RollbackException());
        replay(users);

        controller.register(gson.toJson(userDTO));
    }

    @Test(expected = NullJsonContentException.class)
    public void registerTestFail1() throws UserExistsException, NullJsonContentException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        users.insert(isA(User.class));
        expectLastCall().andThrow(new NullPointerException());
        replay(users);

        controller.register(gson.toJson(userDTO));
    }

    @Test
    public void loginTestSuccess() throws UserNotExistsException, WrongPasswordException, NullJsonContentException {
        Gson gson = new Gson();
        User user = new User("Folea", "Folea", "1234");
        user.setId(5);
        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        expect(users.getUserByUsername("Folea")).andReturn(user);
        replay(users);

        isA(Integer.class);
        assertEquals(0, controller.login(gson.toJson(userDTO)));
    }

    @Test(expected = UserNotExistsException.class)
    public void loginTestFail1() throws UserNotExistsException, WrongPasswordException, NullJsonContentException {
        Gson gson = new Gson();
        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        expect(users.getUserByUsername("Folea")).andThrow(new UserNotExistsException());
        replay(users);

        controller.login(gson.toJson(userDTO));
    }

    @Test(expected = WrongPasswordException.class)
    public void loginTestFail2() throws UserNotExistsException, WrongPasswordException, NullJsonContentException {
        Gson gson = new Gson();
        User user = new User("Folea", "Folea", "12");
        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        expect(users.getUserByUsername("Folea")).andReturn(user);
        replay(users);

        controller.login(gson.toJson(userDTO));
    }

    @Test(expected = NullJsonContentException.class)
    public void loginTestFail3() throws UserNotExistsException, WrongPasswordException, NullJsonContentException {
        Gson gson = new Gson();
        User user = new User("Folea", "Folea", "12");
        UserDTO userDTO = new UserDTO("Folea", "Folea", "1234");

        expect(users.getUserByUsername("Folea")).andThrow(new NullPointerException());
        replay(users);

        controller.login(gson.toJson(userDTO));
    }

    @Test
    public void sendMessageTestSuccess() throws TokenNotExistsException, UserNotExistsException, NullJsonContentException {
        Gson gson = new Gson();
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent("hello");
        messageDTO.setToUser("Cristi");
        Token token = new Token(new User("Folea", "Folea", "1234"));
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andReturn(token);
        expect(users.getUserByUsername("Cristi")).andReturn(new User("Cristi", "Cristi", "1234"));

        replay(tokens);
        replay(users);
        replay(messages);

        controller.sendMessage(gson.toJson(messageDTO), 5);
    }

    @Test(expected = UserNotExistsException.class)
    public void sendMessageTestFail1() throws UserNotExistsException, TokenNotExistsException, NullJsonContentException {
        Gson gson = new Gson();
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent("hello");
        messageDTO.setToUser("Cristi");
        Token token = new Token(new User("Folea", "Folea", "1234"));
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andReturn(token);
        expect(users.getUserByUsername("Cristi")).andThrow(new UserNotExistsException());

        replay(tokens);
        replay(users);
        replay(messages);

        controller.sendMessage(gson.toJson(messageDTO), 5);
    }

    @Test(expected = TokenNotExistsException.class)
    public void sendMessageTestFail2() throws UserNotExistsException, TokenNotExistsException, NullJsonContentException {
        Gson gson = new Gson();
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent("hello");
        messageDTO.setToUser("Cristi");
        Token token = new Token(new User("Folea", "Folea", "1234"));
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andThrow(new TokenNotExistsException());
        expect(users.getUserByUsername("Cristi")).andReturn(new User("Cristi", "Cristi", "1234"));

        replay(tokens);
        replay(users);
        replay(messages);

        controller.sendMessage(gson.toJson(messageDTO), 5);
    }

    @Test(expected = NullJsonContentException.class)
    public void sendMessageTestFail3() throws UserNotExistsException, TokenNotExistsException, NullJsonContentException {
        Gson gson = new Gson();
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent("hello");
        messageDTO.setToUser("Cristi");
        Token token = new Token(new User("Folea", "Folea", "1234"));
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andThrow(new NullPointerException());
        expect(users.getUserByUsername("Cristi")).andReturn(new User("Cristi", "Cristi", "1234"));

        replay(tokens);
        replay(users);
        replay(messages);

        controller.sendMessage(gson.toJson(messageDTO), 5);
    }

    @Test
    public void getMessagesTestSuccess() throws TokenNotExistsException {
        Token token = new Token(new User("Folea", "Folea", "1234"));
        token.setGUID(5);
        LinkedList<Message> listMessages = new LinkedList<Message>();
        listMessages.add(new Message("Hello", new User("Folea", "Folea", "1234"), new User("Cristi", "Cristi", "1234")));
        expect(tokens.getTokenById(5)).andReturn(token);
        expect(messages.getRecvMessage(new User("Folea", "Folea", "1234"))).andReturn(listMessages);

        replay(tokens);
        replay(messages);

        assertEquals("Hello", controller.getMessages(5).get(0).getContent());
    }

    @Test(expected = TokenNotExistsException.class)
    public void getMessagesTestFail1() throws TokenNotExistsException {
        Token token = new Token(new User("Folea", "Folea", "1234"));
        token.setGUID(5);
        LinkedList<Message> listMessages = new LinkedList<Message>();
        listMessages.add(new Message("Hello", new User("Folea", "Folea", "1234"), new User("Cristi", "Cristi", "1234")));
        expect(tokens.getTokenById(5)).andThrow(new TokenNotExistsException());
        expect(messages.getRecvMessage(new User("Folea", "Folea", "1234"))).andReturn(listMessages);

        replay(tokens);
        replay(messages);

        controller.getMessages(5).get(0).getContent();
    }

    @Test
    public void getMessageByIdSuccess() throws TokenNotExistsException, MessageNotExistsException {
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        Message message = new Message("Hello", new User("Folea", "Folea", "1234"), new User("Cristi", "Cristi", "1234"));
        message.setId(10);
        expect(tokens.getTokenById(5)).andReturn(token);
        expect(messages.getMessageById(10, 1)).andReturn(message);

        replay(tokens);
        replay(messages);

        assertEquals(10, controller.getMessageById(10, 5).getId());
    }

    @Test(expected = TokenNotExistsException.class)
    public void getMessageByIdFail1() throws TokenNotExistsException, MessageNotExistsException {
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        Message message = new Message("Hello", new User("Folea", "Folea", "1234"), new User("Cristi", "Cristi", "1234"));
        message.setId(10);
        expect(tokens.getTokenById(5)).andThrow(new TokenNotExistsException());
        expect(messages.getMessageById(10, 1)).andReturn(message);

        replay(tokens);
        replay(messages);

        assertEquals(10, controller.getMessageById(10, 5).getId());
    }

    @Test(expected = MessageNotExistsException.class)
    public void getMessageByIdFail2() throws TokenNotExistsException, MessageNotExistsException {
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        Message message = new Message("Hello", new User("Folea", "Folea", "1234"), new User("Cristi", "Cristi", "1234"));
        message.setId(10);
        expect(tokens.getTokenById(5)).andReturn(token);
        expect(messages.getMessageById(10, 1)).andThrow(new MessageNotExistsException());

        replay(tokens);
        replay(messages);

        assertEquals(10, controller.getMessageById(10, 5).getId());
    }

    @Test
    public void createPostSuccess() throws TokenNotExistsException, NullJsonContentException {
        Gson gson = new Gson();
        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent("Hello");
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andReturn(token);

        replay(tokens);
        replay(publications);

        controller.createPost(gson.toJson(publicationDTO, PublicationDTO.class), 5);
    }

    @Test(expected = TokenNotExistsException.class)
    public void createPostFail1() throws TokenNotExistsException, NullJsonContentException {
        Gson gson = new Gson();
        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent("Hello");
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andThrow(new TokenNotExistsException());

        replay(tokens);
        replay(publications);

        controller.createPost(gson.toJson(publicationDTO, PublicationDTO.class), 5);
    }

    @Test(expected = NullJsonContentException.class)
    public void createPostFail2() throws TokenNotExistsException, NullJsonContentException {
        Gson gson = new Gson();
        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent("Hello");
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andThrow(new NullPointerException());

        replay(tokens);
        replay(publications);

        controller.createPost(gson.toJson(publicationDTO, PublicationDTO.class), 5);
    }

    @Test
    public void getPostByIdAndUserSuccess() throws TokenNotExistsException, PublicationNotExistsException {
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andReturn(token);

        Post post = new Post("content", user);
        post.setId(10);
        expect(publications.getPublicationByIdAndUser(10, user.getId())).andReturn(post);

        replay(tokens);
        replay(publications);

        assertEquals(10, controller.getPostByIdAndUser(10, 5).getId());
    }

    @Test(expected = TokenNotExistsException.class)
    public void getPostByIdAndUserFail1() throws TokenNotExistsException, PublicationNotExistsException {
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andThrow(new TokenNotExistsException());

        Post post = new Post("content", user);
        post.setId(10);
        expect(publications.getPublicationByIdAndUser(10, user.getId())).andReturn(post);

        replay(tokens);
        replay(publications);

        assertEquals(10, controller.getPostByIdAndUser(10, 5).getId());
    }

    @Test(expected = PublicationNotExistsException.class)
    public void getPostByIdAndUserFail2() throws TokenNotExistsException, PublicationNotExistsException {
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andReturn(token);

        Post post = new Post("content", user);
        post.setId(10);
        expect(publications.getPublicationByIdAndUser(10, user.getId())).andThrow(new PublicationNotExistsException());

        replay(tokens);
        replay(publications);

        assertEquals(10, controller.getPostByIdAndUser(10, 5).getId());
    }

    @Test
    public void getPostsSuccess() throws TokenNotExistsException {
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andReturn(token);

        LinkedList<Publication> listPublications = new LinkedList<>();
        Post post = new Post("content", user);
        post.setId(10);
        listPublications.add(post);
        expect(publications.getPostsByUser(token.getUser())).andReturn(listPublications);

        replay(tokens);
        replay(publications);

        assertEquals(10, controller.getPosts(token.getGUID()).get(0).getId());
    }

    @Test(expected = TokenNotExistsException.class)
    public void getPostsFail() throws TokenNotExistsException {
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andThrow(new TokenNotExistsException());

        LinkedList<Publication> listPublications = new LinkedList<>();
        Post post = new Post("content", user);
        post.setId(10);
        listPublications.add(post);
        expect(publications.getPostsByUser(token.getUser())).andReturn(listPublications);

        replay(tokens);
        replay(publications);

        assertEquals(10, controller.getPosts(token.getGUID()).get(0).getId());
    }

    @Test
    public void commentPostSuccess() throws TokenNotExistsException, PublicationNotExistsException, NullJsonContentException {
        Gson gson = new Gson();
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andReturn(token);

        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent("Hello");
        publicationDTO.setPost(10);

        Post post = new Post();
        post.setId(10);

        expect(publications.getPublicationById(10)).andReturn(post);

        replay(tokens);
        replay(publications);

        controller.commentPost(gson.toJson(publicationDTO), token.getGUID());
    }

    @Test(expected = TokenNotExistsException.class)
    public void commentPostFail1() throws TokenNotExistsException, PublicationNotExistsException, NullJsonContentException {
        Gson gson = new Gson();
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andThrow(new TokenNotExistsException());

        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent("Hello");
        publicationDTO.setPost(10);

        Post post = new Post();
        post.setId(10);

        expect(publications.getPublicationById(10)).andReturn(post);

        replay(tokens);
        replay(publications);

        controller.commentPost(gson.toJson(publicationDTO), token.getGUID());
    }

    @Test(expected = PublicationNotExistsException.class)
    public void commentPostFail2() throws TokenNotExistsException, PublicationNotExistsException, NullJsonContentException {
        Gson gson = new Gson();
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andReturn(token);

        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent("Hello");
        publicationDTO.setPost(10);

        Post post = new Post();
        post.setId(10);

        expect(publications.getPublicationById(10)).andThrow(new PublicationNotExistsException());

        replay(tokens);
        replay(publications);

        controller.commentPost(gson.toJson(publicationDTO), token.getGUID());
    }

    @Test(expected = NullJsonContentException.class)
    public void commentPostFail3() throws TokenNotExistsException, PublicationNotExistsException, NullJsonContentException {
        Gson gson = new Gson();
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andReturn(token);

        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent("Hello");
        publicationDTO.setPost(10);

        Post post = new Post();
        post.setId(10);

        expect(publications.getPublicationById(10)).andThrow(new NullPointerException());

        replay(tokens);
        replay(publications);

        controller.commentPost(gson.toJson(publicationDTO), token.getGUID());
    }

    @Test
    public void getCommentsSuccess() {
        User user = new User("Folea", "Folea", "1234");
        Post post = new Post();
        post.setId(10);
        LinkedList<Publication> comments = new LinkedList<>();
        Comment comment = new Comment(user, "Hello", post);
        comment.setId(5);
        comments.add(comment);

        expect(publications.getCommentByPost(post)).andReturn(comments);

        replay(publications);

        assertEquals(5, controller.getComments(post).get(0).getId());
    }

    @Test
    public void likePublicationSuccess() throws TokenNotExistsException, PublicationNotExistsException, LikeAlreadyExistsException, NullJsonContentException {
        Gson gson = new Gson();
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andReturn(token);

        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setPost(10);

        Post post = new Post();
        post.setId(10);

        expect(publications.getPublicationById(10)).andReturn(post);

        replay(tokens);
        replay(publications);

        controller.likePublication(gson.toJson(publicationDTO), token.getGUID());
    }

    @Test(expected = TokenNotExistsException.class)
    public void likePublicationFail1() throws TokenNotExistsException, PublicationNotExistsException, LikeAlreadyExistsException, NullJsonContentException {
        Gson gson = new Gson();
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andThrow(new TokenNotExistsException());

        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setPost(10);

        Post post = new Post();
        post.setId(10);

        expect(publications.getPublicationById(10)).andReturn(post);

        replay(tokens);
        replay(publications);

        controller.likePublication(gson.toJson(publicationDTO), token.getGUID());
    }

    @Test(expected = PublicationNotExistsException.class)
    public void likePublicationFail2() throws TokenNotExistsException, PublicationNotExistsException, LikeAlreadyExistsException, NullJsonContentException {
        Gson gson = new Gson();
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andReturn(token);

        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setPost(10);

        Post post = new Post();
        post.setId(10);

        expect(publications.getPublicationById(10)).andThrow(new PublicationNotExistsException());

        replay(tokens);
        replay(publications);

        controller.likePublication(gson.toJson(publicationDTO), token.getGUID());
    }

    @Test(expected = LikeAlreadyExistsException.class)
    public void likePublicationFail3() throws TokenNotExistsException, PublicationNotExistsException, LikeAlreadyExistsException, NullJsonContentException {
        Gson gson = new Gson();
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andReturn(token);

        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setPost(10);

        Post post = new Post();
        post.setId(10);

        expect(publications.getPublicationById(10)).andReturn(post);

        likes.insert(isA(Likes.class));
        expectLastCall().andThrow(new RollbackException());

        replay(tokens);
        replay(publications);
        replay(likes);

        controller.likePublication(gson.toJson(publicationDTO), token.getGUID());
    }
    @Test(expected = NullJsonContentException.class)
    public void likePublicationFail4() throws TokenNotExistsException, PublicationNotExistsException, LikeAlreadyExistsException, NullJsonContentException {
        Gson gson = new Gson();
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        expect(tokens.getTokenById(5)).andReturn(token);

        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setPost(10);

        Post post = new Post();
        post.setId(10);

        expect(publications.getPublicationById(10)).andThrow(new NullPointerException());

        likes.insert(isA(Likes.class));
        expectLastCall().andThrow(new RollbackException());

        replay(tokens);
        replay(publications);
        replay(likes);

        controller.likePublication(gson.toJson(publicationDTO), token.getGUID());
    }


    @Test
    public void getLikesForPublicationSuccess() {
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Token token = new Token(user);
        token.setGUID(5);
        Post post = new Post();
        post.setId(10);
        LinkedList<Likes> listLikes = new LinkedList<>();
        Likes like = new Likes(post, token.getUser());
        like.setId(15);
        listLikes.add(like);

        expect(likes.getLikesForPublication(post)).andReturn(listLikes);

        replay(likes);

        assertEquals(15, controller.getLikesForPublication(post).get(0).getId());
    }
}
