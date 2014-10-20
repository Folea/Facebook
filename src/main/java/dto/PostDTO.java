package dto;

import java.util.LinkedList;
import java.util.List;

/**
 * The PostDTO class it's used to transfer data between the received json and the DB.
 */

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }
}
