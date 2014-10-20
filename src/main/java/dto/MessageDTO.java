package dto;

/**
 * The MessageDTO class it's used to transfer data between the received json and the DB.
 */

public class MessageDTO {

    private String toUser;
    private String fromUser;
    private String content;

    public MessageDTO() {
    }

    public MessageDTO(String toUser, String fromUser, String content) {
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.content = content;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getContent() {
        return content;
    }
}
