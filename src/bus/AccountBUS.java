package bus;

import java.util.List;
import dao.AccountDAO;
import dto.AccountDTO;

public class AccountBUS {
    
    private AccountDAO accountDAO;
    
    public AccountBUS() {
        this.accountDAO = new AccountDAO();
    }
    
    public List<AccountDTO> getAll() {
        return accountDAO.GetAllAccount();
    }
    
    public AccountDTO getById(int id) {
        return accountDAO.GetAccountById(id);
    }
    

    
    public boolean add(AccountDTO account) {
        if (!validateAccount(account)) return false;
        return accountDAO.AddAccount(account);
    }
    
    public boolean update(AccountDTO account) {
        if (account.getID() <= 0) return false;
        if (!validateAccountForUpdate(account)) return false;
        return accountDAO.EditAccount(account);
    }
    
    public boolean updatePassword(int id, String newPassword) {
        if (id <= 0 || newPassword == null || newPassword.trim().isEmpty()) return false;
        return accountDAO.UpdatePassword(id, newPassword);
    }
    
    public boolean delete(int id) {
        if (id <= 0) return false;
        return accountDAO.DeleteAccount(id);
    }
    
    private boolean validateAccount(AccountDTO account) {
        if (account == null) return false;
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) return false;
        if (account.getPassword() == null || account.getPassword().trim().isEmpty()) return false;
        if (account.getFullname() == null || account.getFullname().trim().isEmpty()) return false;
        if (account.getRole() == null || account.getRole().trim().isEmpty()) return false;
        return true;
    }
    
    private boolean validateAccountForUpdate(AccountDTO account) {
        if (account == null) return false;
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) return false;
        if (account.getFullname() == null || account.getFullname().trim().isEmpty()) return false;
        if (account.getRole() == null || account.getRole().trim().isEmpty()) return false;
        return true;
    }
    
    public boolean isUsernameExists(String username) {
        return accountDAO.IsUsernameExists(username);
    }
    
    public boolean isUsernameExistsExcept(String username, int excludeId) {
        return accountDAO.IsUsernameExistsExcept(username, excludeId);
    }
    
    public AccountDTO login(String username, String password) {
        if (username == null || username.trim().isEmpty()) return null;
        if (password == null || password.trim().isEmpty()) return null;
        
        return accountDAO.Login(username, password);
    }
}
