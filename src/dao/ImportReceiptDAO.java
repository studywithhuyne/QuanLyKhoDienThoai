package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.ImportReceiptDTO;
import utils.DatabaseHelper;

public class ImportReceiptDAO {
    
    // Lấy tất cả phiếu nhập với thông tin nhà cung cấp và nhân viên
    public List<ImportReceiptDTO> GetAllImportReceipt() {
        List<ImportReceiptDTO> receipts = new ArrayList<>();
        String sql = "SELECT ir.id, ir.supplier_id, ir.staff_id, ir.total_amount, ir.created_at, " +
                     "s.name as supplier_name, a.fullname as staff_name " +
                     "FROM import_receipts ir " +
                     "JOIN suppliers s ON ir.supplier_id = s.id " +
                     "JOIN accounts a ON ir.staff_id = a.id " +
                     "ORDER BY ir.id ASC";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                ImportReceiptDTO receipt = new ImportReceiptDTO();
                receipt.setID(rs.getInt("id"));
                receipt.setSupplierId(rs.getInt("supplier_id"));
                receipt.setStaffId(rs.getInt("staff_id"));
                receipt.setTotalAmount(rs.getDouble("total_amount"));
                receipt.setCreatedAt(rs.getTimestamp("created_at"));
                receipt.setSupplierName(rs.getString("supplier_name"));
                receipt.setStaffName(rs.getString("staff_name"));
                receipts.add(receipt);
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return receipts;
    }
    
    // Lấy phiếu nhập theo id
    public ImportReceiptDTO GetImportReceiptById(int id) {
        String sql = "SELECT ir.id, ir.supplier_id, ir.staff_id, ir.total_amount, ir.created_at, " +
                     "s.name as supplier_name, a.fullname as staff_name " +
                     "FROM import_receipts ir " +
                     "JOIN suppliers s ON ir.supplier_id = s.id " +
                     "JOIN accounts a ON ir.staff_id = a.id " +
                     "WHERE ir.id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                ImportReceiptDTO receipt = new ImportReceiptDTO();
                receipt.setID(rs.getInt("id"));
                receipt.setSupplierId(rs.getInt("supplier_id"));
                receipt.setStaffId(rs.getInt("staff_id"));
                receipt.setTotalAmount(rs.getDouble("total_amount"));
                receipt.setCreatedAt(rs.getTimestamp("created_at"));
                receipt.setSupplierName(rs.getString("supplier_name"));
                receipt.setStaffName(rs.getString("staff_name"));
                
                rs.close();
                statement.close();
                return receipt;
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Thêm phiếu nhập mới
    public int AddImportReceipt(ImportReceiptDTO receipt) {
        String sql = "INSERT INTO import_receipts(supplier_id, staff_id, total_amount) VALUES (?, ?, ?)";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, receipt.getSupplierId());
            statement.setInt(2, receipt.getStaffId());
            statement.setDouble(3, receipt.getTotalAmount());
            
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    int newId = rs.getInt(1);
                    rs.close();
                    statement.close();
                    return newId;
                }
            }
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    // Cập nhật phiếu nhập
    public boolean EditImportReceipt(ImportReceiptDTO receipt) {
        String sql = "UPDATE import_receipts SET supplier_id = ?, total_amount = ? WHERE id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, receipt.getSupplierId());
            statement.setDouble(2, receipt.getTotalAmount());
            statement.setInt(3, receipt.getID());
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa phiếu nhập (cascade sẽ xóa chi tiết)
    public boolean DeleteImportReceipt(int id) {
        String sql = "DELETE FROM import_receipts WHERE id = ?";
        
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
    
    // Cập nhật tổng tiền phiếu nhập
    public boolean UpdateTotalAmount(int receiptId, double totalAmount) {
        String sql = "UPDATE import_receipts SET total_amount = ? WHERE id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setDouble(1, totalAmount);
            statement.setInt(2, receiptId);
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
