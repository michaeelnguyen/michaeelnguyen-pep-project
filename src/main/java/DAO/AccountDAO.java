package DAO;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    /**
     * insert method utilizes the DAO associated with creating and inserting a new user/account into the database.
     * @param account The Account class object which contains the account username and password.
     * @return The Account object that contains the data associated with the account after insertion into the database.
     */
    public Account insert(Account account){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    int id = rs.getInt(1);
                    account.setAccount_id(id);
                } else {
                    throw new SQLException("Exception: No account id found");
                }
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return account;
    }

    /**
     * checkUsernameExists method utilizes the DAO associated with retrieving the username in the account table of the database.
     * @return True/False whether or not a username already exists in the database.
     */
    public boolean checkUsernameExists(String username){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE username = ?";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()){
                    return rs.next();
            } catch (SQLException e){
                throw new SQLException("Fail to retrieve username from account table");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * validateLogin method utilizes the DAO to check and validate whether the account credentials match the account in the database.
     * @param loginAccount The Account class object which contains the account username and password credentials.
     * @return The Account object that contains the data associated with the account after validating the account credentials
     * with the one in the database.
     */
    public Account validateLogin(Account loginAccount) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE username = ?";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, loginAccount.getUsername());

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    Account accountFromDb = new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));

                    if(accountFromDb.getPassword().equals(loginAccount.getPassword()))
                    {
                        return accountFromDb;
                    }
                }
            } catch (SQLException e) {
                throw new SQLException("Account Login credentials failed");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * getAccountByID method utilizes the DAO associated with retrieving the account in the database using its account_id.
     * @param id The account_id associated with a specific account in the database.
     * @return The Account object that contains the data associated with the account after database retrieval.
     */
    public Account getAccountByID(int id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE account_id = ?";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));

                }
            } catch (SQLException e) {
                throw new SQLException("Failed to retrieve account_id.");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
