package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import models.AccountType;
import exceptions.NotFoundException;

public interface AccountTypeDAO{
    public AccountType getObject(Connection conn, int account_type_id) throws NotFoundException, SQLException;
    public void load(Connection conn, AccountType valueObject) throws NotFoundException, SQLException;
    public List<AccountType> loadAll(Connection conn) throws SQLException;
    public List<AccountType> searchMatching(Connection conn, AccountType valueObject) throws SQLException;
    public void singleQuery(Connection conn, PreparedStatement stmt, AccountType valueObject) throws NotFoundException, SQLException;
    public List<AccountType> listQuery(Connection conn, PreparedStatement stmt) throws SQLException;
}
