package dto;

import java.util.LinkedList;
import java.util.List;

public class PostDTO {

    private String fromUser;
    private String content;
    private List<PublicationDTO> comments;
    private int likes;

    public PostDTO() {
    }

    public PostDTO(String fromUser, String content, int likes) {
        this.fromUser = fromUser;
        this.content = content;
        this.likes = likes;
        comments = new LinkedList<>();
    }

    public void addComment(PublicationDTO comment) {
        comments.add(comment);
    }
}
