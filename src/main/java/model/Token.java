package model;


import javax.persistence.*;

/**
 * Token represents a token.
 */

@Entity
@NamedQueries({
        @NamedQuery(name = "Token.getTokenById", query = "select t from Token as t where t.GUID = :guid"),
        @NamedQuery(name = "Token.removeTokensUser", query = "delete from Token t where t.user.id = :user")
})
public class Token {

    @Id
    @GeneratedValue(generator = "system-uuid")
    private int GUID;

    @OneToOne
    private User user;

    public Token() {
    }

    public Token(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getGUID() {
        return GUID;
    }

    public void setGUID(int GUID) {
        this.GUID = GUID;
    }
}
