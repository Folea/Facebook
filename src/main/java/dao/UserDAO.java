package dao;

import model.User;
import my_exceptions.UserNotExistsExcepiton;

public interface UserDAO {

    public void insert(User user) /*throws UserExistsException*/;

    public User getUserByUsername(String name) throws UserNotExistsExcepiton;

    public User getUserById(int id);

}
