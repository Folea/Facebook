package model;

import org.eclipse.persistence.annotations.Index;

import javax.persistence.*;

/**
 * Publication represents a publication.
 */

@Inheritance
@Entity
@NamedQueries({
        @NamedQuery(name = "Publication.getPublicationByIdAndUser", query = "select p from Post as p where p.id = :publication and p.fromUser.id = :user"),
        @NamedQuery(name = "Publication.getPublicationById", query = "select p from Post as p where p.id = :publication"),
        @NamedQuery(name = "Publication.getPostByUser", query = "select p from Post as p where p.fromUser.id = :user"),
        @NamedQuery(name = "Publication.getCommentByPost", query = "select c from Comment as c where c.publication.id = :publication")
})
@Index(name = "ID_USER_INDEX", columnNames = {"ID", "FROM_USER"})
public class Publication {

    @Index
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;

    @Basic
    private String content;

    public Publication() {
    }

    public Publication(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
