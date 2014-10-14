package model;

import javax.persistence.*;

@Entity
@NamedQuery(name = "Likes.getLikesByPublication", query = "select l from Likes as l where l.publication.id = :publication")
public class Likes {

    @Id
    @GeneratedValue
    private int id;
    @OneToOne
    private User fromUser;
    @OneToOne
    private Publication publication;

    public Likes() {
    }

    public Likes(Publication publication, User fromUser) {
        this.fromUser = fromUser;
        this.publication = publication;
    }

    public User getFromUser() {
        return fromUser;
    }

    public Publication getPublication() {
        return publication;
    }

}
