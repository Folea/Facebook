package dao.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import dao.TokenDAO;
import model.Token;
import model.User;
import my_exceptions.TokenNotExistsException;
import my_exceptions.UserNotExistsExcepiton;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Singleton
public class TokenDAOImpl implements TokenDAO {

    private EntityManager em;

    @Inject
    public TokenDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void insert(Token token) {
        Query query = em.createNamedQuery("Token.removeTokensUser");
        query.setParameter("user", token.getUser().getId());
        query.executeUpdate();
        em.persist(token);
    }

    @Override
    @Transactional
    public Token getTokenById(int id) throws TokenNotExistsException {
        Token token = null;
        try {
            TypedQuery<Token> q = em.createNamedQuery("Token.getTokenById", Token.class);
            q.setParameter("guid", id);
            token = q.getSingleResult();
        } catch (NoResultException ex) {
            throw new TokenNotExistsException(ex);
        }
        return token;
    }
}
