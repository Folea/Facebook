package dao;

import model.Publication;
import model.User;

import java.util.List;

public interface PublicationDAO {

    public void insert(Publication publication);

    public Publication getPublicationById(int id);

    public List<Publication> getPostsByUser(User user);

    public List<Publication> getCommentByPost(Publication publication);

}
