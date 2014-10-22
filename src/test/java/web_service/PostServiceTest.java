package web_service;

import com.google.gson.Gson;
import controller.Controller;
import dto.PostDTO;
import dto.PublicationDTO;
import dto.ReturnDTO;
import model.*;
import my_exceptions.PublicationNotExistException;
import my_exceptions.TokenNotExistsException;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.easymock.EasyMock.*;
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

        assertEquals(gson.toJson("Token is incorrect " + 5), postService.getPosts(5));
    }

    @Test
    public void getPostByIdTestSuccess() throws PublicationNotExistException, TokenNotExistsException {
        Gson gson = new Gson();

        User user = new User("Folea", "Folea", "1234");
        User user1 = new User("Cristi", "Cristi", "1234");

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
        expect(controller.getLikesForPublication(post)).andReturn(likesComment);
        expect(controller.getLikesForPublication(comment)).andReturn(likesComment);
        replay(controller);

        postService.setController(controller);

        PostDTO postDTO = new PostDTO(( post).getFromUser().getUsername(), post.getContent(), likesPost.size());
        for (Publication c : comments) {
            PublicationDTO publicationDTO = new PublicationDTO(c.getContent(), likesComment.size());
            postDTO.addComment(publicationDTO);
        }

        assertEquals(gson.toJson(postDTO, PostDTO.class), postService.getPostById(2, 1));
    }

    @Test
    public void getPostByIdTestFail1() throws PublicationNotExistException, TokenNotExistsException {
        Gson gson = new Gson();

        User user = new User("Folea", "Folea", "1234");
        User user1 = new User("Cristi", "Cristi", "1234");

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

        expect(controller.getPostByIdAndUser(2, 1)).andThrow(new PublicationNotExistException());
        expect(controller.getComments(post)).andReturn(comments);
        expect(controller.getLikesForPublication(post)).andReturn(likesComment);
        expect(controller.getLikesForPublication(comment)).andReturn(likesComment);
        replay(controller);

        postService.setController(controller);

        PostDTO postDTO = new PostDTO(( post).getFromUser().getUsername(), post.getContent(), likesPost.size());
        for (Publication c : comments) {
            PublicationDTO publicationDTO = new PublicationDTO(c.getContent(), likesComment.size());
            postDTO.addComment(publicationDTO);
        }

        assertEquals(gson.toJson("Publication id doesn't exist " + post.getId()), postService.getPostById(2, 1));
    }

    @Test
    public void getPostByIdTestFail2() throws PublicationNotExistException, TokenNotExistsException {
        Gson gson = new Gson();

        User user = new User("Folea", "Folea", "1234");
        User user1 = new User("Cristi", "Cristi", "1234");

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

        expect(controller.getPostByIdAndUser(2, 1)).andThrow(new TokenNotExistsException());
        expect(controller.getComments(post)).andReturn(comments);
        expect(controller.getLikesForPublication(post)).andReturn(likesComment);
        expect(controller.getLikesForPublication(comment)).andReturn(likesComment);
        replay(controller);

        postService.setController(controller);

        PostDTO postDTO = new PostDTO(( post).getFromUser().getUsername(), post.getContent(), likesPost.size());
        for (Publication c : comments) {
            PublicationDTO publicationDTO = new PublicationDTO(c.getContent(), likesComment.size());
            postDTO.addComment(publicationDTO);
        }

        assertEquals(gson.toJson("Token is incorrect " + 1), postService.getPostById(2, 1));
    }

    @Test
    public void createPostSuccess() throws TokenNotExistsException {
        Gson gson = new Gson();

        User user = new User("Folea", "Folea", "1234");

        PostDTO post = new PostDTO();
        post.setContent("Hello");
        post.setFromUser("Folea");

        expect(controller.createPost(gson.toJson(post, PostDTO.class), 2)).andReturn(5);
        replay(controller);

        postService.setController(controller);

        ReturnDTO returnDTO = new ReturnDTO(5, "Post has been created successful");

        assertEquals(gson.toJson(returnDTO, ReturnDTO.class), postService.createPost(gson.toJson(post, PostDTO.class), 2));
    }

    @Test
    public void createPostFail() throws TokenNotExistsException {
        Gson gson = new Gson();

        User user = new User("Folea", "Folea", "1234");

        PostDTO post = new PostDTO();
        post.setContent("Hello");
        post.setFromUser("Folea");

        expect(controller.createPost(gson.toJson(post, PostDTO.class), 2)).andThrow(new TokenNotExistsException());
        replay(controller);

        postService.setController(controller);

        assertEquals(gson.toJson("The user is not connected"), postService.createPost(gson.toJson(post, PostDTO.class), 2));
    }

}
