package model;

import org.eclipse.persistence.annotations.Index;

import javax.persistence.*;

/**
 * User represents a user.
 */

@Entity
@NamedQueries({
        @NamedQuery(name = "User.getUserById", query = "select u from User as u where u.id = :user"),
        @NamedQuery(name = "User.getUserByUsername", query = "select u from User as u where u.username = :user")
})

public class User {

    @Index
    @Id
    @GeneratedValue
    private int id;

    @Basic
    private String name;

    @Index
    @Basic
    @Column(unique = true)
    private String username;

    @Basic
    private String password;


    public User() {
    }

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;

        return true;
    }
}