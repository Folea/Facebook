package model;

import javax.persistence.*;

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
