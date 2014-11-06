package controller;

import com.google.gson.Gson;
import com.google.inject.Inject;
import dao.*;
import dto.MessageDTO;
import dto.PublicationDTO;
import dto.UserDTO;
import model.*;
import my_exceptions.*;

import javax.persistence.RollbackException;
import java.util.List;

/**
 * Controller class provides all the functionality of the application
 */

public class Controller {

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

    /**
     * getUserByToken it's used to get a User that a token represents.
     *
     * @param token The token for which return the user.
     * @return The user for the specify token.
     * @throws TokenNotExistsException If the id doesn't exist, throws this exception
     */

    public User getUserByToken(int token) throws TokenNotExistsException {
        return tokens.getTokenById(token).getUser();
    }

    /**
     * register it's used to register a used. The information about the user to register it's provide by a JSON.
     *
     * @param json The JSON that contains the user information.
     * @throws UserExistsException If the user already exists.
     */

    public void register(String json) throws UserExistsException, NullJsonContentException {
        Gson gson = new Gson();

        try {
            User user = gson.fromJson(json, User.class);
            users.insert(user);
        } catch (NullPointerException ex) {
            throw new NullJsonContentException(ex);
        } catch (RollbackException ex) {
            throw new UserExistsException(ex);
        }
    }

    /**
     * Login method it's used to login into the application.The user information is provided by a JSON.
     *
     * @param json The JSON that contains the user information.
     * @return If the user exists and the password match return the user id.
     * @throws UserNotExistsException If the user doesn't exist in the DB.
     * @throws WrongPasswordException If the passwo
     */

    public int login(String json) throws UserNotExistsException, WrongPasswordException, NullJsonContentException {
        User user;
        Gson gson = new Gson();
        try {
            user = users.getUserByUsername(gson.fromJson(json, UserDTO.class).getUsername());
            if (user.getPassword().compareTo(gson.fromJson(json, UserDTO.class).getPassword()) != 0) {
                throw new WrongPasswordException();
            } else {
                Token token = new Token(user);
                tokens.insert(token);
                return token.getGUID();
            }
        } catch (NullPointerException ex) {
            throw new NullJsonContentException(ex);
        }
    }

    /**
     * sendMesage it's used to send a message that contains the information extracted from the JSON. The token
     * it's used to identify the user that wants to send the message.
     *
     * @param json  The JSON that contains the message information.
     * @param token The token which identify the user which want to send the message.
     * @return return the id from the send message.
     * @throws UserNotExistsException  If the user doesn't exist in the DB.
     * @throws TokenNotExistsException If the token doesn't exist in the DB.
     */

    public int sendMessage(String json, int token) throws UserNotExistsException, TokenNotExistsException, NullJsonContentException {
        Gson gson = new Gson();
        try {
            Message message = new Message(gson.fromJson(json, MessageDTO.class).getContent(), tokens.getTokenById(token).getUser(),
                    users.getUserByUsername(gson.fromJson(json, MessageDTO.class).getToUser()));
            messages.insert(message);
            return message.getId();
        } catch (NullPointerException ex) {
            throw new NullJsonContentException(ex);
        }
    }

    /**
     * getMessages retrieve the messages received by the user connected with the token.
     *
     * @param token The token that identify the connected user.
     * @return returns a list of messages.
     * @throws TokenNotExistsException If the tokens doesn't exist in the DB.
     */

    public List<Message> getMessages(int token) throws TokenNotExistsException {
        return messages.getRecvMessage(tokens.getTokenById(token).getUser());
    }

    /**
     * getMessageById receive the message with the specify id.
     *
     * @param id    The id of the message to retrieve.
     * @param token The token of the connected user.
     * @return A message.
     * @throws TokenNotExistsException   If the token doesn't exist in the DB.
     * @throws MessageNotExistsException If the message doesn't exist in the DB.
     */

    public Message getMessageById(int id, int token) throws TokenNotExistsException, MessageNotExistsException {
        User user = tokens.getTokenById(token).getUser();
        return messages.getMessageById(id, user.getId());
    }

    /**
     * createPost creates a post with the information provides by the JSON. The user that creates the post is the user
     * that march with the token.
     *
     * @param json  Post data.
     * @param token The user token.
     * @return The id of the created post.
     * @throws TokenNotExistsException If the token doesn't exist.
     */

    public int createPost(String json, int token) throws TokenNotExistsException, NullJsonContentException {
        Gson gson = new Gson();
        try {
            String content = gson.fromJson(json, PublicationDTO.class).getContent();
            Post post = new Post(content, tokens.getTokenById(token).getUser());
            publications.insert(post);
            return post.getId();
        } catch (NullPointerException ex) {
            throw new NullJsonContentException(ex);
        }
    }

    /**
     * getPostByIdAndUser retrieves a publication(Post or Comment) identified by the publication id and the connected
     * user that it's identified by the token.
     *
     * @param id    The id of the publication.
     * @param token The token that identify the connected user.
     * @return The publication with the specified id.
     * @throws TokenNotExistsException                     If the token doesn't exist in the DB.
     * @throws my_exceptions.PublicationNotExistsException If the publication doesn't exist in the DB.
     */

    public Publication getPostByIdAndUser(int id, int token) throws TokenNotExistsException, PublicationNotExistsException {
        User user = tokens.getTokenById(token).getUser();
        return publications.getPublicationByIdAndUser(id, user.getId());
    }

    /**
     * getPost returns al the the post posted by the connected user.
     *
     * @param token The token that identify the connected user.
     * @return List of post.
     * @throws TokenNotExistsException If the token doesn't exist in the DB.
     */

    public List<Publication> getPosts(int token) throws TokenNotExistsException {
        return publications.getPostsByUser(tokens.getTokenById(token).getUser());
    }

    /**
     * commentPost it's used to comment a specified post with the content that the JSON provides.
     *
     * @param json  The JSOn that contains the id of the post to be commented and the content of the comment.
     * @param token The token that identify the connected user.
     * @return The id of the comment.
     * @throws TokenNotExistsException                     If the token doesn't exist in the DB.
     * @throws my_exceptions.PublicationNotExistsException If the publication doesn't exist in the DB.
     */

    public int commentPost(String json, int token) throws TokenNotExistsException, PublicationNotExistsException,
            NullJsonContentException {
        Gson gson = new Gson();
        try {
            Comment comment = new Comment(tokens.getTokenById(token).getUser(), gson.fromJson(json,
                    PublicationDTO.class).getContent(), publications.getPublicationById(gson.fromJson(json,
                    PublicationDTO.class).getPost()));
            publications.insert(comment);
            return comment.getId();
        } catch (NullPointerException ex) {
            throw new NullJsonContentException(ex);
        }
    }

    /**
     * getComments returns all the comments for a specified publication.
     *
     * @param publication The publication for which get the comments.
     * @return List of comments.
     */

    public List<Publication> getComments(Publication publication) {
        return publications.getCommentByPost(publication);
    }

    /**
     * likePublication it's used to put a like for a publication whose id it's in JSON.
     *
     * @param json  The Json that contains the like content.
     * @param token The token that identify the connected user.
     * @return The id of the created like.
     * @throws TokenNotExistsException                     If the token doesn't exist.
     * @throws my_exceptions.PublicationNotExistsException If the publication doesn't exist.
     * @throws my_exceptions.LikeAlreadyExistsException    If the like already exist.
     */

    public int likePublication(String json, int token) throws TokenNotExistsException, PublicationNotExistsException,
            LikeAlreadyExistsException, NullJsonContentException {
        Gson gson = new Gson();
        try {
            Likes like = new Likes(publications.getPublicationById(gson.fromJson(json, PublicationDTO.class).getPost()),
                    tokens.getTokenById(token).getUser());
            likes.insert(like);
            return like.getId();
        } catch (RollbackException ex) {
            throw new LikeAlreadyExistsException(ex);
        } catch (NullPointerException ex) {
            throw new NullJsonContentException(ex);
        }

    }

    /**
     * getLikesForPublication retrieves the likes for a publication.
     *
     * @param publication the publication for which retrieves the likes.
     * @return List of likes.
     */

    public List<Likes> getLikesForPublication(Publication publication) {
        return likes.getLikesForPublication(publication);
    }
}