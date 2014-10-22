package dao.impl;

import dao.dao.impl.PublicationDAOImpl;
import model.Post;
import model.Publication;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.easymock.EasyMock.createNiceMock;

public class PublicationDAOImplTest {

    PublicationDAOImpl publicationDAO;
    EntityManager em;

    @Before
    public void setUp() {
        em = createNiceMock(EntityManager.class);
        publicationDAO = new PublicationDAOImpl(em);
    }

    @Test
    public void insertSuccess() {
        Publication publication = new Post();
        publicationDAO.insert(publication);
    }
}
