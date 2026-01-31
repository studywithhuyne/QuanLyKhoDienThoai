package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.InvoiceDTO;
import dto.InvoiceDetailDTO;
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
    
    // Lấy chi tiết phiếu xuất theo invoice_id
    public List<InvoiceDetailDTO> GetInvoiceDetails(int invoiceId) {
        List<InvoiceDetailDTO> details = new ArrayList<>();
        String sql = "SELECT id.id, id.invoice_id, id.sku_id, id.quantity, id.imei_id, " +
                     "s.code as sku_code, s.price, p.name as product_name, " +
                     "pi.imei as imei_code " +
                     "FROM invoice_details id " +
                     "JOIN skus s ON id.sku_id = s.id " +
                     "JOIN products p ON s.product_id = p.id " +
                     "LEFT JOIN phone_imeis pi ON id.imei_id = pi.id " +
                     "WHERE id.invoice_id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, invoiceId);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                InvoiceDetailDTO detail = new InvoiceDetailDTO();
                detail.setId(rs.getInt("id"));
                detail.setInvoiceId(rs.getInt("invoice_id"));
                detail.setSkuId(rs.getInt("sku_id"));
                detail.setQuantity(rs.getInt("quantity"));
                
                int imeiId = rs.getInt("imei_id");
                detail.setImeiId(rs.wasNull() ? null : imeiId);
                
                detail.setSkuCode(rs.getString("sku_code"));
                detail.setPrice(rs.getDouble("price"));
                detail.setProductName(rs.getString("product_name"));
                detail.setImeiCode(rs.getString("imei_code"));
                
                details.add(detail);
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return details;
    }
    
    // Thêm chi tiết phiếu xuất
    public boolean AddInvoiceDetail(int invoiceId, int skuId, int quantity, Integer imeiId) {
        String sql = "INSERT INTO invoice_details (invoice_id, sku_id, quantity, imei_id) VALUES (?, ?, ?, ?)";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, invoiceId);
            statement.setInt(2, skuId);
            statement.setInt(3, quantity);
            if (imeiId != null) {
                statement.setInt(4, imeiId);
            } else {
                statement.setNull(4, java.sql.Types.INTEGER);
            }
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Giảm tồn kho SKU khi xuất
    public boolean DecreaseSkuStock(int skuId, int quantityToDecrease) {
        String sql = "UPDATE skus SET stock = stock - ? WHERE id = ? AND stock >= ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, quantityToDecrease);
            statement.setInt(2, skuId);
            statement.setInt(3, quantityToDecrease);
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật trạng thái IMEI khi bán
    public boolean UpdateImeiStatus(int imeiId, String status) {
        String sql = "UPDATE phone_imeis SET status = ? WHERE id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, status);
            statement.setInt(2, imeiId);
            
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
