package dto;

public class PublicationDTO {

    private String content;
    private int post;
    private int likes;

    /**
     * The PublicationDTO class it's used to transfer data between the received json and the DB
     */

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

    public void setContent(String content) {
        this.content = content;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
