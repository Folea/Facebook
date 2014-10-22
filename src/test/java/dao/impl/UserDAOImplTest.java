package dao.impl;

import dao.dao.impl.UserDAOImpl;
import model.User;
import my_exceptions.UserNotExistsException;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;


import static org.easymock.EasyMock.*;

public class UserDAOImplTest {

    EntityManager em;
    UserDAOImpl userDAOImpl;

    @Before
    public void setUp() {
        em = createNiceMock(EntityManager.class);
        userDAOImpl = new UserDAOImpl(em);
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


    }
}
