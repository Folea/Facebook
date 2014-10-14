package model;

import javax.persistence.*;

@Inheritance
@Entity
@NamedQueries({
        @NamedQuery(name = "Publication.getPublicationById", query = "select p from Post as p where p.id = :publication"),
        @NamedQuery(name = "Publication.getPostByUser", query = "select p from Post as p where p.fromUser.id = :user"),
        @NamedQuery(name = "Publication.getCommentByPost", query = "select c from Comment as c where c.publication.id = :publication")
})
public class Publication {

    @Id
    @GeneratedValue
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
}
