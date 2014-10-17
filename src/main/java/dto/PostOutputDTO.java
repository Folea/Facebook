package dto;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by cristian.folea on 17.10.2014.
 */
public class PostOutputDTO {

    private String fromUser;
    private String content;
    private List<PublicationDTO> comments;
    private int likes;

    public PostOutputDTO() {
    }

    public PostOutputDTO(String fromUser, String content, int likes) {
        this.fromUser = fromUser;
        this.content = content;
        this.likes = likes;
        comments = new LinkedList<>();
    }

    public void addComment(PublicationDTO comment) {
        comments.add(comment);
    }
}
