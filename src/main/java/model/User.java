package model;

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

    @Id
    @GeneratedValue
    private int id;
    @Basic
    private String name;
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

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}