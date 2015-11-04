package dao;

import model.Likes;
import model.Publication;

import java.util.List;

public interface LikesDAO {

    public void insert(Likes like);

    public List<Likes> getLikesForPublication(Publication publication);

}
