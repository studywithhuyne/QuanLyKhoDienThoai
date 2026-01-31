package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import dto.ImeiDTO;
import utils.DatabaseHelper;

public class ImeiDAO {
    
    // Lấy tất cả IMEI với thông tin SKU và sản phẩm
    public List<ImeiDTO> GetAllImei() {
        List<ImeiDTO> imeis = new ArrayList<>();
        String sql = "SELECT pi.id, pi.sku_id, pi.import_receipt_id, pi.imei, pi.status, pi.created_at, " +
                     "s.code as sku_code, p.name as product_name " +
                     "FROM phone_imeis pi " +
                     "JOIN skus s ON pi.sku_id = s.id " +
                     "JOIN products p ON s.product_id = p.id " +
                     "ORDER BY pi.id ASC";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                ImeiDTO imei = new ImeiDTO();
                imei.setID(rs.getInt("id"));
                imei.setSkuId(rs.getInt("sku_id"));
                imei.setImportReceiptId(rs.getInt("import_receipt_id"));
                imei.setImei(rs.getString("imei"));
                imei.setStatus(rs.getString("status"));
                imei.setCreatedAt(rs.getTimestamp("created_at"));
                imei.setSkuCode(rs.getString("sku_code"));
                imei.setProductName(rs.getString("product_name"));
                imeis.add(imei);
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return imeis;
    }
    
    // Lấy IMEI theo SKU id
    public List<ImeiDTO> GetImeisBySkuId(int skuId) {
        List<ImeiDTO> imeis = new ArrayList<>();
        String sql = "SELECT id, sku_id, import_receipt_id, imei, status, created_at " +
                     "FROM phone_imeis WHERE sku_id = ? ORDER BY id";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, skuId);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                ImeiDTO imei = new ImeiDTO();
                imei.setID(rs.getInt("id"));
                imei.setSkuId(rs.getInt("sku_id"));
                imei.setImportReceiptId(rs.getInt("import_receipt_id"));
                imei.setImei(rs.getString("imei"));
                imei.setStatus(rs.getString("status"));
                imei.setCreatedAt(rs.getTimestamp("created_at"));
                imeis.add(imei);
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return imeis;
    }
    
    // Lấy IMEI còn hàng theo SKU id
    public List<ImeiDTO> GetAvailableImeisBySkuId(int skuId) {
        List<ImeiDTO> imeis = new ArrayList<>();
        String sql = "SELECT id, sku_id, import_receipt_id, imei, status, created_at " +
                     "FROM phone_imeis WHERE sku_id = ? AND status = 'available' ORDER BY id";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, skuId);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                ImeiDTO imei = new ImeiDTO();
                imei.setID(rs.getInt("id"));
                imei.setSkuId(rs.getInt("sku_id"));
                imei.setImportReceiptId(rs.getInt("import_receipt_id"));
                imei.setImei(rs.getString("imei"));
                imei.setStatus(rs.getString("status"));
                imei.setCreatedAt(rs.getTimestamp("created_at"));
                imeis.add(imei);
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return imeis;
    }
    
    // Thêm IMEI mới
    public boolean AddImei(ImeiDTO imei) {
        String sql = "INSERT INTO phone_imeis(sku_id, import_receipt_id, imei, status) VALUES (?, ?, ?, ?)";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, imei.getSkuId());
            statement.setInt(2, imei.getImportReceiptId());
            statement.setString(3, imei.getImei());
            statement.setString(4, imei.getStatus() != null ? imei.getStatus() : "available");
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật IMEI
    public boolean EditImei(ImeiDTO imei) {
        String sql = "UPDATE phone_imeis SET sku_id = ?, imei = ?, status = ?, created_at = ? WHERE id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, imei.getSkuId());
            statement.setString(2, imei.getImei());
            statement.setString(3, imei.getStatus());
            statement.setTimestamp(4, imei.getCreatedAt());
            statement.setInt(5, imei.getID());
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật trạng thái IMEI
    public boolean UpdateStatus(int id, String status) {
        String sql = "UPDATE phone_imeis SET status = ? WHERE id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, status);
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
    
    // Xóa IMEI
    public boolean DeleteImei(int id) {
        String sql = "DELETE FROM phone_imeis WHERE id = ?";
        
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
    
    // Kiểm tra IMEI đã tồn tại
    public boolean IsImeiExists(String imei) {
        String sql = "SELECT COUNT(*) FROM phone_imeis WHERE imei = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, imei);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                statement.close();
                return count > 0;
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Kiểm tra IMEI đã tồn tại (trừ id hiện tại)
    public boolean IsImeiExistsExcept(String imei, int exceptId) {
        String sql = "SELECT COUNT(*) FROM phone_imeis WHERE imei = ? AND id != ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, imei);
            statement.setInt(2, exceptId);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                statement.close();
                return count > 0;
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Lấy IMEI khả dụng (status = 'available') theo SKU id
    public List<ImeiDTO> GetAvailableImeiBySkuId(int skuId) {
        List<ImeiDTO> imeis = new ArrayList<>();
        String sql = "SELECT id, sku_id, import_receipt_id, imei, status, created_at " +
                     "FROM phone_imeis WHERE sku_id = ? AND status = 'available' ORDER BY id";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, skuId);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                ImeiDTO imei = new ImeiDTO();
                imei.setID(rs.getInt("id"));
                imei.setSkuId(rs.getInt("sku_id"));
                imei.setImportReceiptId(rs.getInt("import_receipt_id"));
                imei.setImei(rs.getString("imei"));
                imei.setStatus(rs.getString("status"));
                imei.setCreatedAt(rs.getTimestamp("created_at"));
                imeis.add(imei);
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return imeis;
    }
}
