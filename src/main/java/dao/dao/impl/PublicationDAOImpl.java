package dao.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import dao.PublicationDAO;
import model.Publication;
import model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * PublicationDAOImpl class implements PublicationDAO interface and provides methods to persist and get publications
 * from the database.
 */

@Singleton
public class PublicationDAOImpl implements PublicationDAO {

    private EntityManager em;

    @Inject
    public PublicationDAOImpl(EntityManager em) {
        this.em = em;
    }

    /**
     * Insert method it's used to persist a publication into the database.
     *
     * @param publication The publication to persist.
     */

    @Override
    @Transactional
    public void insert(Publication publication) {
        em.persist(publication);
    }

    /**
     * GetPublicationById it's used to get a publication with the indicate id.
     *
     * @param id The id of the publication.
     * @return Publication
     */

    @Override
    @Transactional
    public Publication getPublicationById(int id) {
        TypedQuery<Publication> q = em.createNamedQuery("Publication.getPublicationById", Publication.class);
        q.setParameter("publication", id);
        return q.getSingleResult();
    }

    /**
     * GetPostByUser it's used to get all the post posted by an user.
     *
     * @param user The user for which get the posts.
     * @return List of Posts
     */

    @Override
    @Transactional
    public List<Publication> getPostsByUser(User user) {
        TypedQuery<Publication> q = em.createNamedQuery("Publication.getPostByUser", Publication.class);
        q.setParameter("user", user.getId());
        return q.getResultList();
    }

    /**
     * GetCommentByPost it's used to get a comment from a post.
     *
     * @param publication
     * @return
     */

    @Override
    @Transactional
    public List<Publication> getCommentByPost(Publication publication) {
        TypedQuery<Publication> q = em.createNamedQuery("Publication.getCommentByPost", Publication.class);
        q.setParameter("publication", publication.getId());
        return q.getResultList();
    }
}
