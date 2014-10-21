package dto;

import dao.UserDAO;

/**
 * The UserDTO class it's used to transfer data between the received json and the DB
 */

public class UserDTO {

    private String username;
    private String name;
    private String password;

    public UserDTO() {

    }

    public UserDTO(String username, String name, String password){
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
