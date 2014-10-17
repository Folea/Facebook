package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue(value = "POST")
public class Post extends Publication {

    @OneToOne
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
}
