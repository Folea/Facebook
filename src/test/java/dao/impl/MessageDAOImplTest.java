package dao.impl;

import dao.dao.impl.MessageDAOImpl;
import model.Message;
import model.User;
import my_exceptions.MessageNotExistsException;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.LinkedList;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class MessageDAOImplTest {

    MessageDAOImpl messageDAOImpl;
    EntityManager em;
    TypedQuery<Message> query;

    @Before
    public void setUp() {
        em = createNiceMock(EntityManager.class);
        messageDAOImpl = new MessageDAOImpl(em);
        query = createNiceMock(TypedQuery.class);
    }

    @Test
    public void insertSuccess() {
        Message message = new Message();
        messageDAOImpl.insert(message);
    }

    @Test
    public void getRecvMessageSuccess() {
        User user = new User("Folea", "Folea", "1234");
        Message message = new Message();
        message.setId(1);
        LinkedList<Message> messages = new LinkedList<>();
        messages.add(message);

        expect(em.createNamedQuery("Message.getRecvMessages", Message.class)).andReturn(query);
        expect(query.getResultList()).andReturn(messages);
        replay(em);
        replay(query);

        assertEquals(1, messageDAOImpl.getRecvMessage(user).get(0).getId());
    }

    @Test
    public void getMessageByIdSuccess() throws MessageNotExistsException {
        Message message = new Message();
        message.setId(1);

        expect(em.createNamedQuery("Message.getMessageById", Message.class)).andReturn(query);
        expect(query.getSingleResult()).andReturn(message);
        replay(em);
        replay(query);

        assertEquals(1, messageDAOImpl.getMessageById(1, 2).getId());
    }

    @Test(expected = MessageNotExistsException.class)
    public void getMessageByIdFail() throws MessageNotExistsException {
        Message message = new Message();
        message.setId(1);

        expect(em.createNamedQuery("Message.getMessageById", Message.class)).andReturn(query);
        expect(query.getSingleResult()).andThrow(new NoResultException());
        replay(em);
        replay(query);

        assertEquals(1, messageDAOImpl.getMessageById(1, 2).getId());
    }


}
