package model;

import javax.persistence.*;

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
}
