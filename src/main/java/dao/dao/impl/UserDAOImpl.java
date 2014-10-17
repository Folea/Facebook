package dao.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import dao.UserDAO;
import model.User;
import my_exceptions.UserNotExistsExcepiton;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Singleton
public class UserDAOImpl implements UserDAO {
    static Logger logger = Logger.getLogger(UserDAOImpl.class);

    private EntityManager em;

    @Inject
    public UserDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void insert(User user) {
        em.persist(user);

    }

    @Override
    @Transactional
    public User getUserByUsername(String name) throws UserNotExistsExcepiton {
        User user = null;
        try {
            TypedQuery<User> q = em.createNamedQuery("User.getUserByUsername", User.class);
            q.setParameter("user", name);
            user = q.getSingleResult();
        } catch (NoResultException ex) {
            logger.info("No result for username " + name);
            throw new UserNotExistsExcepiton(ex);
        }
        return user;
    }

    @Override
    @Transactional
    public User getUserById(int id) throws UserNotExistsExcepiton {
        User user = null;
        try {
            TypedQuery<User> q = em.createNamedQuery("User.getUserById", User.class);
            q.setParameter("user", id);
            user = q.getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("No result for id " + id);
            throw new UserNotExistsExcepiton(ex);
        }
        return user;
    }
}
