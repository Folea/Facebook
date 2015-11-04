package dao;

import model.Token;
import my_exceptions.TokenNotExistsException;

public interface TokenDAO {

    public void insert(Token token);

    public Token getTokenById(int id) throws TokenNotExistsException;

}
