package dao.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import dao.MessageDAO;
import model.Message;
import model.User;
import my_exceptions.MessageNotExistsException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * MessageDAOImpl class implements MessageDAO interface and provides methods to persist and get messages from the database.
 */

@Singleton
public class MessageDAOImpl implements MessageDAO {

    private EntityManager em;

    @Inject
    public MessageDAOImpl(EntityManager em) {
        this.em = em;
    }

    /**
     * Insert method it's used to persist a message into the database.
     *
     * @param message The message to persist
     */

    @Override
    @Transactional
    public void insert(Message message) {
        em.persist(message);
    }

    /**
     * GetRecvMessage method it's used to get the messages received by an user.
     *
     * @param user The user for which to get the messages.
     * @return List of messages.
     */

    @Override
    @Transactional
    public List<Message> getRecvMessage(User user) {
        TypedQuery<Message> q = em.createNamedQuery("Message.getRecvMessages", Message.class);
        q.setParameter("user", user.getId());
        return q.getResultList();
    }

    @Override
    public Message getMessageById(int id, int user) throws MessageNotExistsException {
        try {
            TypedQuery<Message> q = em.createNamedQuery("Message.getMessageById", Message.class);
            q.setParameter("id", id);
            q.setParameter("user", user);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            throw new MessageNotExistsException(ex);
        }
    }
}
