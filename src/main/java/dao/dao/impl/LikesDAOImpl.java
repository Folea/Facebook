package dao.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import dao.LikesDAO;
import model.Likes;
import model.Publication;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * LikesDAOImpl class implements LikesDAO interface and provides methods to persist and get likes from the database.
 */

@Singleton
public class LikesDAOImpl implements LikesDAO {

    private EntityManager em;

    @Inject
    public LikesDAOImpl(EntityManager em) {
        this.em = em;
    }

    /**
     * Insert method it's used to persist a like.
     * @param like The like to persist.
     */

    @Override
    @Transactional
    public void insert(Likes like) {
        em.persist(like);
    }

    /**
     * GetLikesForPublication method it's used to get the likes for a publication(comment or post). It get the likes
     * by the publication id.
     * @param publication The publication for which get the likes;
     * @return List of likes.
     */

    @Override
    @Transactional
    public List<Likes> getLikesForPublication(Publication publication) {
        TypedQuery<Likes> q = em.createNamedQuery("Likes.getLikesByPublication", Likes.class);
        q.setParameter("publication", publication.getId());
        return q.getResultList();
    }
}
