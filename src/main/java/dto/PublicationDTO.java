package dto;

public class PublicationDTO {

    private String content;
    private int post;
    private int likes;

    public PublicationDTO() {
    }

    public PublicationDTO(String content, int likes) {
        this.content = content;
        this.likes = likes;
    }

    public PublicationDTO(String content, int post, int likes) {
        this.content = content;
        this.post = post;
        this.likes = likes;
    }

    public String getContent() {
        return content;
    }

    public int getPost() {
        return post;
    }
}
