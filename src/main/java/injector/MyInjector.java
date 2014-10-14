package injector;

import com.google.inject.AbstractModule;
import dao.*;
import dao.dao.impl.*;

public class MyInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserDAO.class).to(UserDAOImpl.class);
        bind(PublicationDAO.class).to(PublicationDAOImpl.class);
        bind(MessageDAO.class).to(MessageDAOImpl.class);
        bind(LikesDAO.class).to(LikesDAOImpl.class);
        bind(TokenDAO.class).to(TokenDAOImpl.class);
    }
}
