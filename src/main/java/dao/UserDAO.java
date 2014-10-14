package dao;

import model.User;

public interface UserDAO {

    public void insert(User user);

    public User getUserByUsername(String name);

    public User getUserById(int id);

}
