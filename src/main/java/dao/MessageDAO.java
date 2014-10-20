package dao;

import model.Message;
import model.User;
import my_exceptions.MessageNotExistsException;

import java.util.List;

public interface MessageDAO {

    public void insert(Message message);

    public List<Message> getRecvMessage(User user);

    public Message getMessageById(int id, int user) throws MessageNotExistsException;

}
