package web_service;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import dto.PostOutputDTO;
import dto.PublicationDTO;
import injector.MyInitializer;
import injector.MyInjector;
import model.Post;
import model.Publication;
import my_exceptions.TokenNotExistsException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;

@Path("/publication")
public class PostService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPosts(@QueryParam("token") int token) {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        Gson gson = new Gson();
        try {
            List<Publication> listPosts = controller.getPosts(token);
            List<PostOutputDTO> listPostsDTO = new LinkedList<PostOutputDTO>();
            for (Publication p : listPosts) {
                PostOutputDTO postOutputDTO = new PostOutputDTO(((Post) p).getFromUser().getUsername(), p.getContent(),
                        controller.getLikesForPublciation(p).size());
                List<Publication> comments = controller.getComments(p);
                for (Publication c : comments) {
                    PublicationDTO publicationDTO = new PublicationDTO(c.getContent(), controller.getLikesForPublciation(c).size());
                    postOutputDTO.addComment(publicationDTO);
                }
                listPostsDTO.add(postOutputDTO);
            }
            return gson.toJson(listPostsDTO, List.class);
        } catch (TokenNotExistsException ex) {
            return gson.toJson("Token is incorrect " + token);
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createPost(String json) {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        Gson gson = new Gson();
        try {
            controller.createPost(json);
            return gson.toJson("Post has been created successful");
        } catch (TokenNotExistsException ex) {
            return gson.toJson("The user is not connected");
        }
    }
}
