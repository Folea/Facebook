package model;

import javax.persistence.*;

@Entity
@NamedQuery(name = "Message.getRecvMessages", query = "select m from Message as m where m.toUser.id = :user")
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

    public User getFromUser() {
        return fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User user) {
        this.toUser = toUser;
    }

    public void setFromUsers(User user) {
        this.toUser = toUser;
    }

    public int getId() {
        return id;
    }
}
