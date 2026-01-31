package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.LogDTO;
import utils.DatabaseHelper;

public class LogDAO {
    
    // Lấy tất cả logs (có join với accounts để lấy username)
    public List<LogDTO> GetAllLogs() {
        List<LogDTO> logs = new ArrayList<>();
        String sql = "SELECT l.id, l.user_id, a.username, l.action, l.details, l.created_at " +
                     "FROM logs l " +
                     "JOIN accounts a ON l.user_id = a.id " +
                     "ORDER BY l.created_at DESC";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                LogDTO log = new LogDTO();
                log.setID(rs.getInt("id"));
                log.setUserId(rs.getInt("user_id"));
                log.setUsername(rs.getString("username"));
                log.setAction(rs.getString("action"));
                log.setDetails(rs.getString("details"));
                log.setCreatedAt(rs.getTimestamp("created_at"));
                logs.add(log);
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return logs;
    }
    
    // Thêm log mới
    public boolean AddLog(LogDTO log) {
        String sql = "INSERT INTO logs(user_id, action, details) VALUES (?, ?, ?)";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, log.getUserId());
            statement.setString(2, log.getAction());
            statement.setString(3, log.getDetails());
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Thêm log tiện lợi với userId, action, details
    public boolean AddLog(int userId, String action, String details) {
        LogDTO log = new LogDTO(userId, action, details);
        return AddLog(log);
    }
    
    // Lấy n logs gần nhất
    public List<LogDTO> GetRecentLogs(int limit) {
        List<LogDTO> logs = new ArrayList<>();
        String sql = "SELECT l.id, l.user_id, a.username, l.action, l.details, l.created_at " +
                     "FROM logs l " +
                     "JOIN accounts a ON l.user_id = a.id " +
                     "ORDER BY l.created_at DESC " +
                     "LIMIT ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, limit);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                LogDTO log = new LogDTO();
                log.setID(rs.getInt("id"));
                log.setUserId(rs.getInt("user_id"));
                log.setUsername(rs.getString("username"));
                log.setAction(rs.getString("action"));
                log.setDetails(rs.getString("details"));
                log.setCreatedAt(rs.getTimestamp("created_at"));
                logs.add(log);
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return logs;
    }
}
