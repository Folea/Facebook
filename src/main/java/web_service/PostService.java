package web_service;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import dto.PostDTO;
import dto.PublicationDTO;
import dto.ReturnDTO;
import injector.MyInitializer;
import injector.MyInjector;
import model.Post;
import model.Publication;
import my_exceptions.PublicationNotExistException;
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
            List<PostDTO> listPostsDTO = new LinkedList<>();
            for (Publication p : listPosts) {
                PostDTO postOutputDTO = new PostDTO(((Post) p).getFromUser().getUsername(), p.getContent(),
                        controller.getLikesForPublication(p).size());
                List<Publication> comments = controller.getComments(p);
                for (Publication c : comments) {
                    PublicationDTO publicationDTO = new PublicationDTO(c.getContent(), controller.getLikesForPublication(c).size());
                    postOutputDTO.addComment(publicationDTO);
                }
                listPostsDTO.add(postOutputDTO);
            }
            return gson.toJson(listPostsDTO, List.class);
        } catch (TokenNotExistsException ex) {
            return gson.toJson("Token is incorrect " + token);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPostById(@PathParam("id") int id, @QueryParam("token") int token) {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        Gson gson = new Gson();
        try {
            Publication post = controller.getPostByIdAndUser(id, token);
            PostDTO postDTO = new PostDTO(((Post) post).getFromUser().getUsername(), post.getContent(), controller.getLikesForPublication(post).size());
            List<Publication> comments = controller.getComments(post);
            for (Publication c : comments) {
                PublicationDTO publicationDTO = new PublicationDTO(c.getContent(), controller.getLikesForPublication(c).size());
                postDTO.addComment(publicationDTO);
            }
            return gson.toJson(postDTO, PostDTO.class);
        } catch (TokenNotExistsException ex) {
            return gson.toJson("Token is incorrect " + token);
        } catch (PublicationNotExistException ex) {
            return gson.toJson("Publication id doesn't exist " + id);
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createPost(String json, @QueryParam("token") int token) {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        Gson gson = new Gson();
        try {
            ReturnDTO returnDTO = new ReturnDTO(controller.createPost(json, token), "Post has been created successful");
            return gson.toJson(returnDTO, ReturnDTO.class);
        } catch (TokenNotExistsException ex) {
            return gson.toJson("The user is not connected");
        }
    }
}
