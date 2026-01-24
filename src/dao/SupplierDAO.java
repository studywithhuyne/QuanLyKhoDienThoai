package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.SupplierDTO;
import utils.DatabaseHelper;

public class SupplierDAO {
    
    // Lấy tất cả nhà cung cấp
    public List<SupplierDTO> GetAllSupplier() {
        List<SupplierDTO> suppliers = new ArrayList<>();
        String sql = "SELECT id, name, phone, email, address FROM suppliers ORDER BY id";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                SupplierDTO supplier = new SupplierDTO();
                supplier.setID(rs.getInt("id"));
                supplier.setName(rs.getString("name"));
                supplier.setPhone(rs.getString("phone"));
                supplier.setEmail(rs.getString("email"));
                supplier.setAddress(rs.getString("address"));
                suppliers.add(supplier);
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return suppliers;
    }
    
    // Thêm nhà cung cấp mới
    public boolean AddSupplier(SupplierDTO supplier) {
        String sql = "INSERT INTO suppliers(name, phone, email, address) VALUES (?, ?, ?, ?)";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getPhone());
            statement.setString(3, supplier.getEmail());
            statement.setString(4, supplier.getAddress());
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật nhà cung cấp
    public boolean EditSupplier(SupplierDTO supplier) {
        String sql = "UPDATE suppliers SET name = ?, phone = ?, email = ?, address = ? WHERE id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getPhone());
            statement.setString(3, supplier.getEmail());
            statement.setString(4, supplier.getAddress());
            statement.setInt(5, supplier.getID());
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa nhà cung cấp
    public boolean DeleteSupplier(int id) {
        String sql = "DELETE FROM suppliers WHERE id = ?";
        
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
    
    // Kiểm tra tên đã tồn tại chưa
    public boolean IsNameExists(String name) {
        String sql = "SELECT COUNT(*) FROM suppliers WHERE name = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
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
    
    // Kiểm tra tên đã tồn tại chưa (ngoại trừ ID hiện tại)
    public boolean IsNameExistsExcept(String name, int excludeId) {
        String sql = "SELECT COUNT(*) FROM suppliers WHERE name = ? AND id != ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
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
}
