package controller;

import com.google.gson.Gson;
import com.google.inject.Inject;
import dao.*;
import dto.MessageDTO;
import dto.PublicationDTO;
import dto.UserDTO;
import model.*;
import my_exceptions.TokenNotExistsException;
import my_exceptions.UserExistsException;
import my_exceptions.UserNotExistsExcepiton;
import my_exceptions.WrongPasswordException;

import javax.persistence.RollbackException;
import java.util.List;

/**
 * Controller class provides all the functionality of the application
 */

public class Controller {

    private int connectedUser = 701;
    private LikesDAO likes;
    private MessageDAO messages;
    private PublicationDAO publications;
    private UserDAO users;
    private TokenDAO tokens;

    /**
     * Controller constructor create a instance for all class parameters.
     *
     * @param likes        Provides the functionality to persist and get likes.
     * @param messages     Provides the functionality to persist and get messages.
     * @param publications Provides the functionality to persist and get publications.
     * @param users        Provides the functionality to persist and get users.
     * @param tokens       Provides the functionality to persist and get tokens.
     */

    @Inject
    public Controller(LikesDAO likes, MessageDAO messages, PublicationDAO publications, UserDAO users, TokenDAO tokens) throws TokenNotExistsException {
        this.likes = likes;
        this.messages = messages;
        this.publications = publications;
        this.users = users;
        this.tokens = tokens;
    }

    public User getUserByToken(int token) throws TokenNotExistsException {
        return tokens.getTokenById(token).getUser();
    }

    public void register(String json) throws UserExistsException {
        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);

        try {
            users.insert(user);
        } catch (RollbackException ex) {
            throw new UserExistsException(ex);
        }

    }

    /**
     * Login method it's used to login into the application. If the user exists create a token and assigns it to the user.
     *
     * @return Returns true if the user exists and the password match, otherwise returns false.
     */

    public boolean login(String json) throws UserNotExistsExcepiton, WrongPasswordException {
        User user;
        Gson gson = new Gson();
        user = users.getUserByUsername(gson.fromJson(json, UserDTO.class).getUsername());
        if (user == null) {
            return false;
        } else if (user.getPassword().compareTo(gson.fromJson(json, UserDTO.class).getPassword()) != 0) {
            throw new WrongPasswordException();
        } else {
            Token token = new Token(user);
            tokens.insert(token);
            return true;
        }
    }

    public void sendMessage(String json) throws UserNotExistsExcepiton, TokenNotExistsException {
        Gson gson = new Gson();
        Message message = new Message(gson.fromJson(json, MessageDTO.class).getContent(), tokens.getTokenById(connectedUser).getUser(),
                users.getUserByUsername(gson.fromJson(json, UserDTO.class).getUsername()));
        messages.insert(message);
    }

    /**
     * GetMessages method returns all the messages received by the logged user.
     *
     * @return List of messages.
     */

    public List<Message> getMessages(int token) throws TokenNotExistsException {
        return messages.getRecvMessage(tokens.getTokenById(token).getUser());
    }

    public void createPost(String json) throws TokenNotExistsException {
        Gson gson = new Gson();
        Post post = new Post(gson.fromJson(json, PublicationDTO.class).getContent(), tokens.getTokenById(701).getUser());
        publications.insert(post);
    }

    /**
     * GetPost method returns all the post posted by the connected user.
     *
     * @return List of posts.
     */

    public List<Publication> getPosts(int token) throws TokenNotExistsException {
        return publications.getPostsByUser(tokens.getTokenById(token).getUser());
    }

    /**
     * CommentPost method it's used to comment a post.
     *
     * @param content The comment of the post.
     * @param id      The id of the post to be commented.
     */

    public void commentPost(String content, int id) throws TokenNotExistsException {
        Comment comment = new Comment(tokens.getTokenById(connectedUser).getUser(), content, publications.getPublicationById(id));
        publications.insert(comment);
    }

    /**
     * GetComments method it's used to get the comments for a publication.
     *
     * @param publication The publication for which to get the comments.
     * @return List of comments.
     */

    public List<Publication> getComments(Publication publication) {
        return publications.getCommentByPost(publication);
    }

    /**
     * LikePublication method it's used to like a publication.
     *
     * @param id Id of the publication to like.
     */

    public void likePublication(int id) throws TokenNotExistsException {
        Likes like = new Likes(publications.getPublicationById(id), tokens.getTokenById(connectedUser).getUser());
        likes.insert(like);
    }

    public List<Likes> getLikesForPublciation(Publication publication) {
        return likes.getLikesForPublication(publication);
    }

}
