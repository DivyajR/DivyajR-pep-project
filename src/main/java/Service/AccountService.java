package Service;
import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    private AccountDAO accountDAO; 

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account){
        
        if(!account.getUsername().isEmpty() && account.getPassword().length() > 4 
        && accountDAO.searchAccount(account.getUsername(), account.getPassword()) == null)
        {
            Account addedAccount = accountDAO.insertAccount(account);
            return addedAccount;
        }
        
        return null;
        
    }

    public Account login(Account account)
    {
        if(accountDAO.searchAccount(account.getUsername(), account.getPassword()) != null)
        {
            Account foundAccount = accountDAO.searchAccount(account.getUsername(), account.getPassword());
            return foundAccount;
        }
        return null;
    }
    
}
