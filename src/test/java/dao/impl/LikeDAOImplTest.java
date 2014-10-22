package dao.impl;

import dao.dao.impl.LikesDAOImpl;
import model.Likes;
import model.Post;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.LinkedList;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

public class LikeDAOImplTest {

    LikesDAOImpl likesDAOImpl;
    EntityManager em;
    TypedQuery<Likes> query;

    @Before
    public void setUp() {
        em = createNiceMock(EntityManager.class);
        likesDAOImpl = new LikesDAOImpl(em);
        query = createNiceMock(TypedQuery.class);
    }

    @Test
    public void insertSuccess() {
        Likes like = new Likes();
        likesDAOImpl.insert(like);
    }

    @Test
    public void getLikesForPublication() {
        Post post = new Post();
        post.setId(2);

        Likes like = new Likes();
        like.setId(1);

        LinkedList<Likes> likes = new LinkedList<>();
        likes.add(like);

        expect(em.createNamedQuery("Likes.getLikesByPublication", Likes.class)).andReturn(query);
        expect(query.getResultList()).andReturn(likes);
        replay(em);
        replay(query);

        assertEquals(1, likesDAOImpl.getLikesForPublication(post).get(0).getId());
    }
}
