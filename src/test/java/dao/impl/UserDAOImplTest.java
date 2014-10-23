package dao.impl;

import dao.dao.impl.UserDAOImpl;
import model.User;
import my_exceptions.UserNotExistsException;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class UserDAOImplTest {

    EntityManager em;
    UserDAOImpl userDAOImpl;
    TypedQuery<User> query;

    @Before
    public void setUp() {
        em = createNiceMock(EntityManager.class);
        userDAOImpl = new UserDAOImpl(em);
        query = createNiceMock(TypedQuery.class);
    }

    @Test
    public void insertSuccess() {
        User user = new User("Folea", "Folea", "1234");
        userDAOImpl.insert(user);
    }

    @Test(expected = RollbackException.class)
    public void insertFail() {
        User user = new User("Folea", "Folea", "1234");

        em.persist(isA(User.class));

        expectLastCall().andThrow(new RollbackException());
        replay(em);

        userDAOImpl.insert(user);
    }

    @Test
    public void getUserByUsernameSuccess() throws UserNotExistsException {
        User user = new User("Folea", "Folea", "1234");

        expect(em.createNamedQuery("User.getUserByUsername", User.class)).andReturn(query);
        expect(query.getSingleResult()).andReturn(user);
        replay(em);
        replay(query);

        assertEquals("Folea", userDAOImpl.getUserByUsername("Folea").getUsername());
    }

    @Test(expected = UserNotExistsException.class)
    public void getUserByUsernameFail() throws UserNotExistsException {
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);

        expect(em.createNamedQuery("User.getUserByUsername", User.class)).andReturn(query);
        expect(query.getSingleResult()).andThrow(new NoResultException());
        replay(em);
        replay(query);

        assertEquals("Folea", userDAOImpl.getUserByUsername("Folea").getUsername());
    }

    @Test
    public void getUserByIdSuccess() throws UserNotExistsException {
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);

        expect(em.createNamedQuery("User.getUserById", User.class)).andReturn(query);
        expect(query.getSingleResult()).andReturn(user);
        replay(em);
        replay(query);

        assertEquals(1, userDAOImpl.getUserById(1).getId());
    }

    @Test(expected = UserNotExistsException.class)
    public void getUserByIdFail() throws UserNotExistsException {
        User user = new User("Folea", "Folea", "1234");
        user.setId(1);

        expect(em.createNamedQuery("User.getUserById", User.class)).andReturn(query);
        expect(query.getSingleResult()).andThrow(new NoResultException());
        replay(em);
        replay(query);

        assertEquals(1, userDAOImpl.getUserById(1).getId());
    }
}
