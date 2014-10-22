package dao.impl;

import dao.dao.impl.LikesDAOImpl;
import model.Likes;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.easymock.EasyMock.createNiceMock;

public class LikeDAOImplTest {

    LikesDAOImpl likesDAOImpl;
    EntityManager em;

    @Before
    public void setUp() {
        em = createNiceMock(EntityManager.class);
        likesDAOImpl = new LikesDAOImpl(em);
    }

    @Test
    public void insertSuccess() {
        Likes like = new Likes();
        likesDAOImpl.insert(like);
    }
}
