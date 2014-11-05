package dao.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import dao.UserDAO;
import model.User;
import my_exceptions.UserNotExistsException;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * UserDAOImpl implements the UserDAO and provides the implementation for the method which allows you
 * to interact with the DB.
 */

@Singleton
public class UserDAOImpl implements UserDAO {
    static Logger logger = Logger.getLogger(UserDAOImpl.class);

    private EntityManager em;

    @Inject
    public UserDAOImpl(EntityManager em) {
        this.em = em;
    }

    /**
     * Insert a new user into the DB.
     * @param user The user to insert.
     */

    @Override
    @Transactional
    public void insert(User user) {
        em.persist(user);
    }

    /**
     * Get a user knowing his username.
     * @param username The username of the user to get.
     * @return Returns the user if it exist.
     * @throws UserNotExistsException  Throws the exception if the user doesn't exist.
     */

    @Override
    @Transactional
    public User getUserByUsername(String username) throws UserNotExistsException {
        try {
            TypedQuery<User> q = em.createNamedQuery("User.getUserByUsername", User.class);
            q.setParameter("user", username);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            logger.info("No result for username " + username);
            throw new UserNotExistsException(ex);
        }
    }

    /**
     * Get a user knowing his id.
     * @param id The id of the user to get.
     * @return The user if it exist.
     * @throws UserNotExistsException Throws the exception if the user doesn't exist.
     */

    @Override
    @Transactional
    public User getUserById(int id) throws UserNotExistsException {
        try {
            TypedQuery<User> q = em.createNamedQuery("User.getUserById", User.class);
            q.setParameter("user", id);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("No result for id " + id);
            throw new UserNotExistsException(ex);
        }
    }
}
