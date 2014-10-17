package dto;

/**
 * Created by cristian.folea on 16.10.2014.
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

    public String getFromUser() {
        return fromUser;
    }

    public String getContent() {
        return content;
    }
}
