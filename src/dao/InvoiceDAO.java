package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.InvoiceDTO;
import utils.DatabaseHelper;

public class InvoiceDAO {
    
    // Lấy tất cả hóa đơn với thông tin nhân viên
    public List<InvoiceDTO> GetAllInvoice() {
        List<InvoiceDTO> invoices = new ArrayList<>();
        String sql = "SELECT i.id, i.staff_id, i.total_amount, i.created_at, a.fullname as staff_name " +
                     "FROM invoices i " +
                     "JOIN accounts a ON i.staff_id = a.id " +
                     "ORDER BY i.id ASC";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                InvoiceDTO invoice = new InvoiceDTO();
                invoice.setID(rs.getInt("id"));
                invoice.setStaffId(rs.getInt("staff_id"));
                invoice.setTotalAmount(rs.getDouble("total_amount"));
                invoice.setCreatedAt(rs.getTimestamp("created_at"));
                invoice.setStaffName(rs.getString("staff_name"));
                invoices.add(invoice);
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return invoices;
    }
    
    // Lấy hóa đơn theo id
    public InvoiceDTO GetInvoiceById(int id) {
        String sql = "SELECT i.id, i.staff_id, i.total_amount, i.created_at, a.fullname as staff_name " +
                     "FROM invoices i " +
                     "JOIN accounts a ON i.staff_id = a.id " +
                     "WHERE i.id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                InvoiceDTO invoice = new InvoiceDTO();
                invoice.setID(rs.getInt("id"));
                invoice.setStaffId(rs.getInt("staff_id"));
                invoice.setTotalAmount(rs.getDouble("total_amount"));
                invoice.setCreatedAt(rs.getTimestamp("created_at"));
                invoice.setStaffName(rs.getString("staff_name"));
                
                rs.close();
                statement.close();
                return invoice;
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Thêm hóa đơn mới
    public int AddInvoice(InvoiceDTO invoice) {
        String sql = "INSERT INTO invoices(staff_id, total_amount) VALUES (?, ?)";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, invoice.getStaffId());
            statement.setDouble(2, invoice.getTotalAmount());
            
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
    
    // Cập nhật hóa đơn
    public boolean EditInvoice(InvoiceDTO invoice) {
        String sql = "UPDATE invoices SET total_amount = ? WHERE id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setDouble(1, invoice.getTotalAmount());
            statement.setInt(2, invoice.getID());
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa hóa đơn (cascade sẽ xóa chi tiết)
    public boolean DeleteInvoice(int id) {
        String sql = "DELETE FROM invoices WHERE id = ?";
        
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
    
    // Cập nhật tổng tiền hóa đơn
    public boolean UpdateTotalAmount(int invoiceId, double totalAmount) {
        String sql = "UPDATE invoices SET total_amount = ? WHERE id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setDouble(1, totalAmount);
            statement.setInt(2, invoiceId);
            
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
