package dao.impl;

import dao.dao.impl.PublicationDAOImpl;
import model.Comment;
import model.Post;
import model.Publication;
import model.User;
import my_exceptions.PublicationNotExistException;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.LinkedList;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class PublicationDAOImplTest {

    PublicationDAOImpl publicationDAO;
    EntityManager em;
    TypedQuery<Publication> query;

    @Before
    public void setUp() {
        em = createNiceMock(EntityManager.class);
        publicationDAO = new PublicationDAOImpl(em);
        query = createNiceMock(TypedQuery.class);
    }

    @Test
    public void insertSuccess() {
        Publication publication = new Post();
        publicationDAO.insert(publication);
    }

    @Test
    public void getPublicationByIdAndUserSuccess() throws PublicationNotExistException {
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Post post = new Post("Hello", user);
        post.setId(2);

        expect(em.createNamedQuery("Publication.getPublicationByIdAndUser", Publication.class)).andReturn(query);
        expect(query.getSingleResult()).andReturn(post);
        replay(em);
        replay(query);

        assertEquals(2, publicationDAO.getPublicationByIdAndUser(2, 1).getId());
    }

    @Test(expected = PublicationNotExistException.class)
    public void getPublicationByIdAndUserFail() throws PublicationNotExistException {
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Post post = new Post("Hello", user);
        post.setId(2);

        expect(em.createNamedQuery("Publication.getPublicationByIdAndUser", Publication.class)).andReturn(query);
        expect(query.getSingleResult()).andThrow(new NoResultException());
        replay(em);
        replay(query);

        assertEquals(2, publicationDAO.getPublicationByIdAndUser(2, 1).getId());
    }

    @Test
    public void getPublicationByIdSuccess() throws PublicationNotExistException {
        Post post = new Post();
        post.setId(2);

        expect(em.createNamedQuery("Publication.getPublicationById", Publication.class)).andReturn(query);
        expect(query.getSingleResult()).andReturn(post);
        replay(em);
        replay(query);

        assertEquals(2, publicationDAO.getPublicationById(2).getId());
    }

    @Test(expected = PublicationNotExistException.class)
    public void getPublicationByIdFail() throws PublicationNotExistException {
        Post post = new Post();
        post.setId(2);

        expect(em.createNamedQuery("Publication.getPublicationById", Publication.class)).andReturn(query);
        expect(query.getSingleResult()).andThrow(new NoResultException());
        replay(em);
        replay(query);

        assertEquals(2, publicationDAO.getPublicationById(2).getId());
    }

    @Test
    public void getPostsByUserSuccess() {
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Post post = new Post("Hello", user);
        post.setId(2);

        LinkedList<Publication> posts = new LinkedList<>();
        posts.add(post);

        expect(em.createNamedQuery("Publication.getPostByUser", Publication.class)).andReturn(query);
        expect(query.getResultList()).andReturn(posts);
        replay(em);
        replay(query);

        assertEquals(1, ((Post) publicationDAO.getPostsByUser(user).get(0)).getFromUser().getId());
    }

    @Test
    public void getCommentByPost() {
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);
        Post post = new Post();
        post.setId(2);
        Comment comment = new Comment();
        comment.setFromUser(user);
        comment.setId(3);

        LinkedList<Publication> comments = new LinkedList<>();
        comments.add(comment);

        expect(em.createNamedQuery("Publication.getCommentByPost", Publication.class)).andReturn(query);
        expect(query.getResultList()).andReturn(comments);
        replay(em);
        replay(query);

        assertEquals(3, ((Comment) publicationDAO.getCommentByPost(post).get(0)).getId());
    }

}
