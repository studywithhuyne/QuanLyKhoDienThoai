package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.AccountDTO;
import utils.DatabaseHelper;

public class AccountDAO {
    
    // Lấy tất cả tài khoản
    public List<AccountDTO> GetAllAccount() {
        List<AccountDTO> accounts = new ArrayList<>();
        String sql = "SELECT id, username, password, role, fullname, last_login, created_at FROM accounts ORDER BY id";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                AccountDTO account = new AccountDTO();
                account.setID(rs.getInt("id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                account.setRole(rs.getString("role"));
                account.setFullname(rs.getString("fullname"));
                account.setLastLogin(rs.getTimestamp("last_login"));
                account.setCreatedAt(rs.getTimestamp("created_at"));
                accounts.add(account);
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }
    
    // Lấy tài khoản theo id
    public AccountDTO GetAccountById(int id) {
        String sql = "SELECT id, username, password, role, fullname, last_login, created_at FROM accounts WHERE id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                AccountDTO account = new AccountDTO();
                account.setID(rs.getInt("id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                account.setRole(rs.getString("role"));
                account.setFullname(rs.getString("fullname"));
                account.setLastLogin(rs.getTimestamp("last_login"));
                account.setCreatedAt(rs.getTimestamp("created_at"));
                
                rs.close();
                statement.close();
                return account;
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Thêm tài khoản mới
    public boolean AddAccount(AccountDTO account) {
        String sql = "INSERT INTO accounts(username, password, role, fullname) VALUES (?, ?, ?, ?)";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.setString(3, account.getRole());
            statement.setString(4, account.getFullname());
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật tài khoản
    public boolean EditAccount(AccountDTO account) {
        String sql = "UPDATE accounts SET username = ?, role = ?, fullname = ? WHERE id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getRole());
            statement.setString(3, account.getFullname());
            statement.setInt(4, account.getID());
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật mật khẩu
    public boolean UpdatePassword(int id, String newPassword) {
        String sql = "UPDATE accounts SET password = ? WHERE id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, newPassword);
            statement.setInt(2, id);
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa tài khoản
    public boolean DeleteAccount(int id) {
        String sql = "DELETE FROM accounts WHERE id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Kiểm tra username đã tồn tại chưa
    public boolean IsUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE username = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Kiểm tra username đã tồn tại chưa (ngoại trừ ID hiện tại)
    public boolean IsUsernameExistsExcept(String username, int excludeId) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE username = ? AND id != ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setInt(2, excludeId);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Đăng nhập
    public AccountDTO Login(String username, String password) {
        String sql = "SELECT id, username, password, role, fullname FROM accounts WHERE username = ? AND password = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                AccountDTO account = new AccountDTO();
                account.setID(rs.getInt("id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                account.setRole(rs.getString("role"));
                account.setFullname(rs.getString("fullname"));
                
                rs.close();
                statement.close();
                
                // Cập nhật last_login
                UpdateLastLogin(account.getID());
                
                return account;
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Cập nhật last_login
    private void UpdateLastLogin(int id) {
        String sql = "UPDATE accounts SET last_login = CURRENT_TIMESTAMP WHERE id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
