package utils;

import dto.AccountDTO;

/**
 * Quản lý phiên đăng nhập của người dùng
 */
public class SessionManager {
    private static SessionManager instance;
    private AccountDTO currentUser;
    
    private SessionManager() {}
    
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    public void setCurrentUser(AccountDTO user) {
        this.currentUser = user;
    }
    
    public AccountDTO getCurrentUser() {
        return currentUser;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public void logout() {
        currentUser = null;
    }
    
    public boolean isAdmin() {
        return currentUser != null && "admin".equals(currentUser.getRole());
    }
    
    public String getDisplayName() {
        if (currentUser == null) return "";
        String fullname = currentUser.getFullname();
        return (fullname != null && !fullname.isEmpty()) ? fullname : currentUser.getUsername();
    }
    
    public String getRoleDisplayName() {
        if (currentUser == null) return "";
        return "admin".equals(currentUser.getRole()) ? "Quản trị viên" : "Nhân viên";
    }
}
