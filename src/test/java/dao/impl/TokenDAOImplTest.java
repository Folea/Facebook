package dao.impl;

import dao.dao.impl.TokenDAOImpl;
import model.Token;
import model.User;
import my_exceptions.TokenNotExistsException;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class TokenDAOImplTest {

    TokenDAOImpl tokenDAOImpl;
    EntityManager em;
    TypedQuery<Token> query;

    @Before
    public void setUp() {
        em = createNiceMock(EntityManager.class);
        tokenDAOImpl = new TokenDAOImpl(em);
        query = createNiceMock(TypedQuery.class);
    }

    @Test
    public void insertSuccess() {
        User user = new User("Folea", "Folea", "1234");
        Token token = new Token(user);
        expect(em.createNamedQuery("Token.removeTokensUser")).andReturn(query);
        replay(em);
        replay(query);
        tokenDAOImpl.insert(token);
    }

    @Test(expected = NoResultException.class)
    public void insertFail() {
        User user = new User("Folea", "Folea", "1234");
        Token token = new Token(user);

        em.persist(isA(Token.class));
        expectLastCall().andThrow(new NoResultException());
        expect(em.createNamedQuery("Token.removeTokensUser")).andReturn(query);
        replay(em);
        replay(query);

        tokenDAOImpl.insert(token);
    }

    @Test
    public void getTokenByIdSuccess() throws TokenNotExistsException {
        User user = new User("Folea", "Folea", "1234");
        Token token = new Token(user);
        token.setGUID(1);

        expect(em.createNamedQuery("Token.getTokenById", Token.class)).andReturn(query);
        expect(query.getSingleResult()).andReturn(token);
        replay(em);
        replay(query);

        assertEquals(1, tokenDAOImpl.getTokenById(1).getGUID());
    }

    @Test(expected = TokenNotExistsException.class)
    public void getTokenByIdFail() throws TokenNotExistsException {
        User user = new User("Folea", "Folea", "1234");
        Token token = new Token(user);
        token.setGUID(1);

        expect(em.createNamedQuery("Token.getTokenById", Token.class)).andReturn(query);
        expect(query.getSingleResult()).andThrow(new NoResultException());
        replay(em);
        replay(query);

        assertEquals(1, tokenDAOImpl.getTokenById(1).getGUID());
    }
}
