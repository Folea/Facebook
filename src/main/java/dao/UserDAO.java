package dao;

import model.User;
import my_exceptions.UserNotExistsException;

public interface UserDAO {

    public void insert(User user) /*throws UserExistsException*/;

    public User getUserByUsername(String name) throws UserNotExistsException;

    public User getUserById(int id) throws UserNotExistsException;

}
