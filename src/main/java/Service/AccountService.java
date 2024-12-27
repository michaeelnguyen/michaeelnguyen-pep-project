package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    /**
     * createAccount method handles the business logic and interaction with the DAO associated
     * with creating and inserting a new user/account into the database.
     * @param account The Account class object which contains account.
     * @return The Account object that contains the data associated with the account after insertion into the database.
     */
    public Account createAccount(Account account){
        try {
            validateAccount(account);

            Account newAccount = new Account(account.getUsername(), account.getPassword());
            
            return accountDAO.insert(newAccount);
            
        } catch (RuntimeException e) {
            throw new RuntimeException("Exception during createAccount");
        }
    }

    /**
     * validateAccount method is a helper function to perform validation on the account requirements.
     * Account username cannot be blank, password less than 4 characters, and user account must exists.
     * @param account The Account class object which contains data associated with the new account.
     */
    public void validateAccount(Account account){
        try{
            String username = account.getUsername().trim();
            String password = account.getPassword().trim();

            // Validation to check if username is blank, password is less than 4 characters, and if username already exists
            if(username.isEmpty()){
                throw new RuntimeException("Username cannot be blank");
            }
            if(password.length() < 4){
                throw new RuntimeException("Password must be at least 4 characters long");
            }
            if(accountDAO.checkUsernameExists(account.getUsername())){
                throw new RuntimeException("Username already exists");
            }

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * login method handles the business logic and interaction with the DAO associated
     * with validating the account login credentials
     * @param account The Account class object which contains the credentials of the login account
     * @return The Account object that contains the data associated with account and login credentials
     */
    public Account login(Account account) {
        try {
            return accountDAO.validateLogin(account);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * getAccountByID method handles the business logic and interaction with the DAO associated
     * with retrieving the account data associated with the account_id
     * @param id The account_id used to retrieve the data of a specific account
     * @return The Account object that contains the data associated with the account after retrieval from the database.
     */
    public Account getAccountByID(int id) {
        try {
            return accountDAO.getAccountByID(id);
        } catch (RuntimeException e){
            e.printStackTrace();
        }
        return null;
    }
}
