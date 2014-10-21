package model;

import javax.persistence.*;

/**
 * Message represents a message.
 */

@Entity
@NamedQueries({
        @NamedQuery(name = "Message.getRecvMessages", query = "select m from Message as m where m.toUser.id = :user"),
        @NamedQuery(name = "Message.getMessageById", query = "select m from Message as m where m.id = :id and m.toUser.id = :user")
})
public class Message {

    @Id
    @GeneratedValue
    private int id;
    @Basic
    private String content;
    @ManyToOne
    private User fromUser;
    @ManyToOne
    private User toUser;

    public Message() {
    }

    public Message(String content, User fromUser, User toUser) {
        this.content = content;
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    public String getContent() {
        return content;
    }

    public User getToUser() {
        return toUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }
}
