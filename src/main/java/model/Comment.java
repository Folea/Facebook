package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * Comment class represents a comment.
 */

@Entity
@DiscriminatorValue(value = "COMMENT")
public class Comment extends Publication {

    @ManyToOne
    private User fromUser;

    @OneToOne
    private Publication publication;

    public Comment() {
    }

    public Comment(User fromUser, String content, Publication publication) {
        super(content);
        this.fromUser = fromUser;
        this.publication = publication;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }
}
