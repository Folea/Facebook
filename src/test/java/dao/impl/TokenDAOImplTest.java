package dao.impl;

import dao.dao.impl.TokenDAOImpl;
import dao.dao.impl.UserDAOImpl;
import model.Token;
import model.User;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.easymock.EasyMock.createNiceMock;

public class TokenDAOImplTest {

    TokenDAOImpl tokenDAOImpl;
    EntityManager em;

    @Before
    public void setUp(){
        em = createNiceMock(EntityManager.class);
        tokenDAOImpl = new TokenDAOImpl(em);
    }

    @Test
    public void insertSuccess() {
        User user = new User("Folea", "Folea", "1234");
        Token token = new Token(user);
    }
}
