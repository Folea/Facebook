package model;


import javax.persistence.*;

/**
 * Likes class represents a like.
 */

@Entity
@NamedQuery(name = "Likes.getLikesByPublication", query = "select l from Likes as l where l.publication.id = :publication")
@Table(
        name = "LIKES",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"FROM_USER", "PUBLICATION"})
)
public class Likes {
    @Id
    @GeneratedValue
    private int id;

    @JoinColumn(name = "FROM_USER")
    @OneToOne
    private User fromUser;

    @org.eclipse.persistence.annotations.Index
    @JoinColumn(name = "PUBLICATION")
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

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
