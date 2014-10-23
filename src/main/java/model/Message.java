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

    public void setContent(String content) {
        this.content = content;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (id != message.id) return false;
        if (content != null ? !content.equals(message.content) : message.content != null) return false;
        if (fromUser != null ? !fromUser.equals(message.fromUser) : message.fromUser != null) return false;
        if (toUser != null ? !toUser.equals(message.toUser) : message.toUser != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (fromUser != null ? fromUser.hashCode() : 0);
        result = 31 * result + (toUser != null ? toUser.hashCode() : 0);
        return result;
    }
}
