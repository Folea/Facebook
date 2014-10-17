package web_service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import controller.Controller;
import injector.MyInitializer;
import injector.MyInjector;
import model.Post;
import model.Publication;
import my_exceptions.TokenNotExistsException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/posts")
public class PostService {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getPosts(@QueryParam("token") int token){
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        try {
            List<Publication> listPosts = controller.getPosts(token);
            String posts = "";
            for (Publication p : listPosts) {
                posts += p.getId() + ". \n" + "Posted by: " + ((Post)p).getFromUser().getUsername() + "\n"
                        + p.getContent() + "\n" + controller.getLikesForPublciation(p).size() + " Likes \n Comments:";
                List<Publication> comments = controller.getComments(p);
                for(Publication c : comments){
                    posts += "\t " + c.getId() + ". \n" + "\tContent: " + c.getContent() + "\n\t"
                            + controller.getLikesForPublciation(c).size() + " Likes \n" ;
                }
            }
            return posts;
        } catch (TokenNotExistsException ex){
            return "Token is incorrect " + token;
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createPost(String json){
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        try{
            controller.createPost(json);
            return "Post has been created successful";
        } catch (TokenNotExistsException ex){
            return "The user is not connected";
        }

    }
}
