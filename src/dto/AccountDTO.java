package dto;

import java.sql.Timestamp;

public class AccountDTO {
    private int id;
    private String username;
    private String password;
    private String role;
    private String fullname;
    private Timestamp lastLogin;
    private Timestamp createdAt;
    
    public AccountDTO() {}
    
    public AccountDTO(int id, String username, String fullname, String role) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.role = role;
    }
    
    // Getters and Setters
    public int getID() {
        return id;
    }
    
    public void setID(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getFullname() {
        return fullname;
    }
    
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
    public Timestamp getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    // Hiển thị trong combobox
    @Override
    public String toString() {
        return fullname != null && !fullname.isEmpty() ? fullname : username;
    }
}
