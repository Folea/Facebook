package model;

import org.eclipse.persistence.annotations.Index;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue(value = "POST")
public class Post extends Publication {

    @Index
    @OneToOne
    @JoinColumn(name = "FROM_USER")
    private User fromUser;

    public Post() {
    }

    public Post(String content, User fromUser) {
        super(content);
        this.fromUser = fromUser;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

}
