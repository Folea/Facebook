package web_service;

import com.google.gson.Gson;
import controller.Controller;
import dto.PostDTO;
import dto.PublicationDTO;
import model.*;
import my_exceptions.PublicationNotExistException;
import my_exceptions.TokenNotExistsException;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

public class PostServiceTest {

    PostService postService;
    Controller controller;

    @Before
    public void setUp(){
        controller = createNiceMock(Controller.class);
        postService = new PostService();
    }

    @Test
    public void getPostsTestSuccess() throws TokenNotExistsException {
        Gson gson = new Gson();

        User user = new User("Folea", "Folea", "1234");
        User user1 = new User("Cristi", "Cristi", "1234");

        Post post = new Post("Hello", user);
        post.setId(2);
        Comment comment = new Comment(user1, "comment", post);
        List<Publication> posts = new LinkedList<>();
        List<Publication> comments = new LinkedList<>();
        posts.add(post);
        comments.add(comment);

        Likes like1 = new Likes(post, user);
        Likes like2 = new Likes(comment, user1);

        List<Likes> likesPost = new LinkedList<>();
        likesPost.add(like1);
        List<Likes> likesComment = new LinkedList<>();
        likesComment.add(like2);

        expect(controller.getPosts(5)).andReturn(posts);
        expect(controller.getComments(post)).andReturn(comments);
        expect(controller.getLikesForPublication(post)).andReturn(likesPost);
        expect(controller.getLikesForPublication(comment)).andReturn(likesComment);
        replay(controller);

        postService.setController(controller);

        List<PostDTO> listPostsDTO = new LinkedList<>();
        for (Publication p : posts) {
            PostDTO postOutputDTO = new PostDTO(((Post) p).getFromUser().getUsername(), p.getContent(),
                    likesPost.size());
            for (Publication c : comments) {
                PublicationDTO publicationDTO = new PublicationDTO(c.getContent(), likesComment.size());
                postOutputDTO.addComment(publicationDTO);
            }
            listPostsDTO.add(postOutputDTO);
        }
        assertEquals(gson.toJson(listPostsDTO, List.class), postService.getPosts(5));
    }

    @Test
    public void getPostsTestFail() throws TokenNotExistsException {
        Gson gson = new Gson();

        User user = new User("Folea", "Folea", "1234");
        User user1 = new User("Cristi", "Cristi", "1234");

        Post post = new Post("Hello", user);
        post.setId(2);
        Comment comment = new Comment(user1, "comment", post);
        List<Publication> posts = new LinkedList<>();
        List<Publication> comments = new LinkedList<>();
        posts.add(post);
        comments.add(comment);

        Likes like1 = new Likes(post, user);
        Likes like2 = new Likes(comment, user1);

        List<Likes> likesPost = new LinkedList<>();
        likesPost.add(like1);
        List<Likes> likesComment = new LinkedList<>();
        likesComment.add(like2);

        expect(controller.getPosts(5)).andThrow(new TokenNotExistsException());
        expect(controller.getComments(post)).andReturn(comments);
        expect(controller.getLikesForPublication(post)).andReturn(likesPost);
        expect(controller.getLikesForPublication(comment)).andReturn(likesComment);
        replay(controller);

        postService.setController(controller);

        List<PostDTO> listPostsDTO = new LinkedList<>();
        for (Publication p : posts) {
            PostDTO postOutputDTO = new PostDTO(((Post) p).getFromUser().getUsername(), p.getContent(),
                    likesPost.size());
            for (Publication c : comments) {
                PublicationDTO publicationDTO = new PublicationDTO(c.getContent(), likesComment.size());
                postOutputDTO.addComment(publicationDTO);
            }
            listPostsDTO.add(postOutputDTO);
        }
        assertEquals(gson.toJson("Token is incorrect "), postService.getPosts(5));
    }

    @Test
    public void getPostByIdTestSuccess() throws PublicationNotExistException, TokenNotExistsException {
        Gson gson = new Gson();
        User user = new User("Folea", "Folea", "1234");
        User user1 = new User("Cristi", "Cristi", "1234");

        Token token = new Token(user);
        token.setGUID(1);

        Post post = new Post("Hello", user);
        post.setId(2);
        Comment comment = new Comment(user1, "comment", post);
        List<Publication> comments = new LinkedList<>();
        comments.add(comment);

        Likes like1 = new Likes(post, user);
        Likes like2 = new Likes(comment, user1);

        List<Likes> likesPost = new LinkedList<>();
        likesPost.add(like1);
        List<Likes> likesComment = new LinkedList<>();
        likesComment.add(like2);

        expect(controller.getPostByIdAndUser(2, 1)).andReturn(post);
        expect(controller.getComments(post)).andReturn(comments);
        expect(controller.getLikesForPublication(post)).andReturn(likesPost);
        expect(controller.getLikesForPublication(comment)).andReturn(likesComment);
        replay(controller);

        postService.setController(controller);

        PostDTO postDTO = new PostDTO(( post).getFromUser().getUsername(), post.getContent(), controller.getLikesForPublication(post).size());
        for (Publication c : comments) {
            PublicationDTO publicationDTO = new PublicationDTO(c.getContent(), likesComment.size());
            postDTO.addComment(publicationDTO);
        }

        assertEquals(gson.toJson(postDTO, PostDTO.class), postService.getPostById(2,1));
    }

}
