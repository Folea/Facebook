package dao.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import dao.TokenDAO;
import model.Token;
import my_exceptions.TokenNotExistsException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * Token implements the TokenDAO and provides the implementation for the method which allows you to interact with the DB.
 */

@Singleton
public class TokenDAOImpl implements TokenDAO {

    private EntityManager em;

    @Inject
    public TokenDAOImpl(EntityManager em) {
        this.em = em;
    }

    /**
     * Insert a token into the DB.
     * @param token The token to insert.
     */

    @Override
    @Transactional
    public void insert(Token token) {
        Query query = em.createNamedQuery("Token.removeTokensUser");
        query.setParameter("user", token.getUser().getId());
        query.executeUpdate();
        em.persist(token);
    }

    /**
     * getTokenById method is used to get a specific token from the DB.
     * @param id The id of the token to get.
     * @return The token if it exist.
     * @throws TokenNotExistsException If the token doesn't exist will throw the exception.
     */

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
