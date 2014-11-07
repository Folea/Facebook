package web_service;

import com.google.gson.Gson;
import controller.Controller;
import dto.PublicationDTO;
import dto.ReturnDTO;
import my_exceptions.NullJsonContentException;
import my_exceptions.PublicationNotExistsException;
import my_exceptions.TokenNotExistsException;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class CommentPostTest {

    CommentPost commentPost;
    Controller controller;

    @Before
    public void setUp() {
        controller = createNiceMock(Controller.class);
        commentPost = new CommentPost();
    }

    @Test
    public void commentPostTestSuccess() throws PublicationNotExistsException, TokenNotExistsException, NullJsonContentException {
        Gson gson = new Gson();
        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent("Hello");
        publicationDTO.setPost(5);
        expect(controller.commentPost(gson.toJson(publicationDTO), 1)).andReturn(5);
        replay(controller);

        commentPost.setController(controller);
        ReturnDTO returnDTO = gson.fromJson(commentPost.commentPost(gson.toJson(publicationDTO), 1), ReturnDTO.class);
        assertEquals(5, returnDTO.getId());
    }

    @Test
    public void commentPostTestFail1() throws PublicationNotExistsException, TokenNotExistsException, NullJsonContentException {
        Gson gson = new Gson();
        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent("Hello");
        publicationDTO.setPost(5);
        expect(controller.commentPost(gson.toJson(publicationDTO), 1)).andThrow(new PublicationNotExistsException());
        replay(controller);

        commentPost.setController(controller);
        assertEquals(gson.toJson("Publication not exist"), commentPost.commentPost(gson.toJson(publicationDTO), 1));
    }

    @Test
    public void commentPostTestFail2() throws PublicationNotExistsException, TokenNotExistsException, NullJsonContentException {
        Gson gson = new Gson();
        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent("Hello");
        publicationDTO.setPost(5);
        expect(controller.commentPost(gson.toJson(publicationDTO), 1)).andThrow(new TokenNotExistsException());
        replay(controller);

        commentPost.setController(controller);
        assertEquals(gson.toJson("Comment fail"), commentPost.commentPost(gson.toJson(publicationDTO), 1));
    }

    @Test
    public void commentPostTestFail3() throws PublicationNotExistsException, TokenNotExistsException, NullJsonContentException {
        Gson gson = new Gson();
        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent("Hello");
        publicationDTO.setPost(5);

        expect(controller.commentPost(null, 1)).andThrow(new NullJsonContentException());
        expect(controller.commentPost("", 1)).andThrow(new NullJsonContentException());
        replay(controller);

        commentPost.setController(controller);
        assertEquals(gson.toJson("The json doesn't contain the expected information"), commentPost.commentPost(null, 1));
    }

    @Test
    public void commentPostTestFail4() throws PublicationNotExistsException, TokenNotExistsException, NullJsonContentException {
        Gson gson = new Gson();
        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent("Hello");
        publicationDTO.setPost(5);

        expect(controller.commentPost("", 1)).andThrow(new NullJsonContentException());
        replay(controller);

        commentPost.setController(controller);
        assertEquals(gson.toJson("The json doesn't contain the expected information"), commentPost.commentPost("", 1));
    }

}
