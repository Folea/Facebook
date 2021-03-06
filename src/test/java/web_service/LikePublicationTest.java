package web_service;

import com.google.gson.Gson;
import controller.Controller;
import dto.PublicationDTO;
import dto.ReturnDTO;
import my_exceptions.LikeAlreadyExistsException;
import my_exceptions.PublicationNotExistsException;
import my_exceptions.TokenNotExistsException;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class LikePublicationTest {

    Controller controller;
    LikePublication likePublication;

    @Before
    public void setUp() {
        controller = createNiceMock(Controller.class);
        likePublication = new LikePublication();
    }

    @Test
    public void likePostSuccess() throws TokenNotExistsException, PublicationNotExistsException, LikeAlreadyExistsException {
        Gson gson = new Gson();
        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setPost(5);
        expect(controller.likePublication(gson.toJson(publicationDTO), 1)).andReturn(10);

        replay(controller);

        likePublication.setController(controller);
        ReturnDTO returnDTO = gson.fromJson(likePublication.likePost(gson.toJson(publicationDTO), 1), ReturnDTO.class);
        assertEquals(10, returnDTO.getId());

    }

    @Test
    public void likePostFail1() throws TokenNotExistsException, PublicationNotExistsException, LikeAlreadyExistsException {
        Gson gson = new Gson();
        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setPost(5);
        expect(controller.likePublication(gson.toJson(publicationDTO), 1)).andThrow(new TokenNotExistsException());

        replay(controller);

        likePublication.setController(controller);
        assertEquals(gson.toJson("You are not logged"), likePublication.likePost(gson.toJson(publicationDTO), 1));

    }

    @Test
    public void likePostFail2() throws TokenNotExistsException, PublicationNotExistsException, LikeAlreadyExistsException {
        Gson gson = new Gson();
        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setPost(5);
        expect(controller.likePublication(gson.toJson(publicationDTO), 1)).andThrow(new PublicationNotExistsException());

        replay(controller);

        likePublication.setController(controller);
        assertEquals(gson.toJson("Publication not exist"), likePublication.likePost(gson.toJson(publicationDTO), 1));
    }

    @Test
    public void likePostFail4() throws TokenNotExistsException, PublicationNotExistsException, LikeAlreadyExistsException {
        Gson gson = new Gson();
        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setPost(5);

        expect(controller.likePublication(gson.toJson(publicationDTO), 1)).andThrow(new LikeAlreadyExistsException());
        replay(controller);

        likePublication.setController(controller);
        assertEquals(gson.toJson("The like already exist"), likePublication.likePost(gson.toJson(publicationDTO), 1));
    }

}
