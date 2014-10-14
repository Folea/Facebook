package dao;

import model.Token;

public interface TokenDAO {

    public void insert(Token token);

    public Token getTokenById(int id);

}
