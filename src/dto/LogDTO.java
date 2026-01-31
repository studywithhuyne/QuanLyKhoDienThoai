package dto;

import java.sql.Timestamp;

public class LogDTO {
    private int id;
    private int userId;
    private String username;
    private String action;
    private String details;
    private Timestamp createdAt;
    
    public LogDTO() {}
    
    public LogDTO(int userId, String action, String details) {
        this.userId = userId;
        this.action = action;
        this.details = details;
    }
    
    // Getters and Setters
    public int getID() {
        return id;
    }
    
    public void setID(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
