package dao;

import model.Publication;
import model.User;
import my_exceptions.PublicationNotExistsException;

import java.util.List;

public interface PublicationDAO {

    public void insert(Publication publication);

    public Publication getPublicationByIdAndUser(int id, int user) throws PublicationNotExistsException;

    public Publication getPublicationById(int id) throws PublicationNotExistsException;

    public List<Publication> getPostsByUser(User user);

    public List<Publication> getCommentByPost(Publication publication);

}
