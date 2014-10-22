package dao.impl;

import dao.dao.impl.MessageDAOImpl;
import model.Message;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.easymock.EasyMock.createNiceMock;

public class MessageDAOImplTest {

    MessageDAOImpl messageDAOImpl;
    EntityManager em;

    @Before
    public void setUp() {
        em = createNiceMock(EntityManager.class);
        messageDAOImpl = new MessageDAOImpl(em);
    }

    @Test
    public void insertSuccess() {
        Message message = new Message();
        messageDAOImpl.insert(message);
    }

}
