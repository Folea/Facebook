package dao;

import model.Message;
import model.User;

import java.util.List;

public interface MessageDAO {

    public void insert(Message message);

    public List<Message> getRecvMessage(User user);

}
