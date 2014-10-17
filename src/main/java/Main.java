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

import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args0) throws TokenNotExistsException {
        Injector injector = Guice.createInjector(new MyInjector(), new JpaPersistModule("facebook"));
        MyInitializer myInitializer = injector.getInstance(MyInitializer.class);
        Controller controller = injector.getInstance(Controller.class);
        Gson gson = new Gson();
        try {
            List<Publication> listPosts = controller.getPosts(651);
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
            String a = gson.toJson(listPostsDTO, List.class);
            System.out.println(a);
        } catch (TokenNotExistsException ex) {
        }

    }
}
