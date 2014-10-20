package dao;

import model.Publication;
import model.User;
import my_exceptions.PublicationNotExistException;

import java.util.List;

public interface PublicationDAO {

    public void insert(Publication publication);

    public Publication getPublicationByIdAndUser(int id, int user) throws PublicationNotExistException;

    public Publication getPublicationById(int id) throws PublicationNotExistException;

    public List<Publication> getPostsByUser(User user);

    public List<Publication> getCommentByPost(Publication publication);

}
