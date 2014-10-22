package dao.impl;

import dao.dao.impl.UserDAOImpl;
import model.User;
import my_exceptions.UserExistsException;
import my_exceptions.UserNotExistsException;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;

import java.util.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

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
        User user = new User("Folea", "Folea", "1234");
        TypedQuery<User> q =  new TypedQuery<User>() {
            @Override
            public List<User> getResultList() {
                return null;
            }

            @Override
            public User getSingleResult() {
                User user = new User("Folea", "Folea", "1234");
                user.setId(2);
                return user;
            }

            @Override
            public TypedQuery<User> setMaxResults(int i) {
                return null;
            }

            @Override
            public TypedQuery<User> setFirstResult(int i) {
                return null;
            }

            @Override
            public TypedQuery<User> setHint(String s, Object o) {
                return null;
            }

            @Override
            public <T> TypedQuery<User> setParameter(Parameter<T> tParameter, T t) {
                return null;
            }

            @Override
            public TypedQuery<User> setParameter(Parameter<Calendar> calendarParameter, Calendar calendar, TemporalType temporalType) {
                return null;
            }

            @Override
            public TypedQuery<User> setParameter(Parameter<Date> dateParameter, Date date, TemporalType temporalType) {
                return null;
            }

            @Override
            public TypedQuery<User> setParameter(String s, Object o) {
                return null;
            }

            @Override
            public TypedQuery<User> setParameter(String s, Calendar calendar, TemporalType temporalType) {
                return null;
            }

            @Override
            public TypedQuery<User> setParameter(String s, Date date, TemporalType temporalType) {
                return null;
            }

            @Override
            public TypedQuery<User> setParameter(int i, Object o) {
                return null;
            }

            @Override
            public TypedQuery<User> setParameter(int i, Calendar calendar, TemporalType temporalType) {
                return null;
            }

            @Override
            public TypedQuery<User> setParameter(int i, Date date, TemporalType temporalType) {
                return null;
            }

            @Override
            public TypedQuery<User> setFlushMode(FlushModeType flushModeType) {
                return null;
            }

            @Override
            public TypedQuery<User> setLockMode(LockModeType lockModeType) {
                return null;
            }

            @Override
            public int executeUpdate() {
                return 0;
            }

            @Override
            public int getMaxResults() {
                return 0;
            }

            @Override
            public int getFirstResult() {
                return 0;
            }

            @Override
            public Map<String, Object> getHints() {
                return null;
            }

            @Override
            public Set<Parameter<?>> getParameters() {
                return null;
            }

            @Override
            public Parameter<?> getParameter(String s) {
                return null;
            }

            @Override
            public <T> Parameter<T> getParameter(String s, Class<T> tClass) {
                return null;
            }

            @Override
            public Parameter<?> getParameter(int i) {
                return null;
            }

            @Override
            public <T> Parameter<T> getParameter(int i, Class<T> tClass) {
                return null;
            }

            @Override
            public boolean isBound(Parameter<?> parameter) {
                return false;
            }

            @Override
            public <T> T getParameterValue(Parameter<T> tParameter) {
                return null;
            }

            @Override
            public Object getParameterValue(String s) {
                return null;
            }

            @Override
            public Object getParameterValue(int i) {
                return null;
            }

            @Override
            public FlushModeType getFlushMode() {
                return null;
            }

            @Override
            public LockModeType getLockMode() {
                return null;
            }

            @Override
            public <T> T unwrap(Class<T> tClass) {
                return null;
            }
        };

        expect(em.createNamedQuery("User.getUserById", User.class)).andReturn(q);
        replay(em);

        assertEquals(2, userDAOImpl.getUserByUsername("Folea").getId());
    }
}
