package controller;

import com.google.gson.Gson;
import com.google.inject.Inject;
import dao.*;
import my_exceptions.UserExistsException;
import model.*;
import my_exceptions.UserNotExistsExcepiton;
import my_exceptions.WrongPasswordException;

import javax.persistence.RollbackException;
import java.util.List;

/**
 * Controller class provides all the functionality of the application
 */

public class Controller {

    private Token connectedUser;
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
    public Controller(LikesDAO likes, MessageDAO messages, PublicationDAO publications, UserDAO users, TokenDAO tokens) {
        this.likes = likes;
        this.messages = messages;
        this.publications = publications;
        this.users = users;
        this.tokens = tokens;
    }



    public void register(String json) throws UserExistsException {
        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);

        try{
            users.insert(user);
        } catch (RollbackException ex) {
            throw new UserExistsException();
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
        user = users.getUserByUsername(gson.fromJson(json, User.class).getUsername());
        if (user == null) {
            return false;
        } else if (user.getPassword().compareTo(gson.fromJson(json, User.class).getPassword()) != 0) {
            throw new WrongPasswordException();
        } else {
            Token token = new Token(user);
            tokens.insert(token);
            connectedUser = token;
            return true;
        }
    }

    /**
     * SendMessage method it's used to create and send a message.
     *
     * @param toUser  The receiver of the message.
     * @param content The content of the message.
     */

    public void sendMessage(String toUser, String content) throws UserNotExistsExcepiton {
        User user = users.getUserByUsername(toUser);
        Message message = new Message(content, connectedUser.getUser(), user);
        messages.insert(message);
    }

    /**
     * GetMessages method returns all the messages received by the logged user.
     *
     * @return List of messages.
     */

    public List<Message> getMessages() {
        return messages.getRecvMessage(connectedUser.getUser());
    }

    /**
     * CreatePost method create a post.
     *
     * @param content The content of the post.
     */

    public void createPost(String content) {
        Post post = new Post(content, connectedUser.getUser());
        publications.insert(post);
    }

    /**
     * GetPost method returns all the post posted by the connected user.
     *
     * @return List of posts.
     */

    public List<Publication> getPosts() {
        return publications.getPostsByUser(connectedUser.getUser());
    }

    /**
     * CommentPost method it's used to comment a post.
     *
     * @param content The comment of the post.
     * @param id      The id of the post to be commented.
     */

    public void commentPost(String content, int id) {
        Comment comment = new Comment(connectedUser.getUser(), content, publications.getPublicationById(id));
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

    public void likePublication(int id) {
        Likes like = new Likes(publications.getPublicationById(id), connectedUser.getUser());
        likes.insert(like);
    }

}
