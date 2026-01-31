package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.ImportReceiptDTO;
import dto.ImportDetailDTO;
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
                     "ORDER BY ir.created_at DESC";
        
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
    
    // Lấy chi tiết phiếu nhập theo import_receipt_id
    public List<ImportDetailDTO> GetImportDetails(int receiptId) {
        List<ImportDetailDTO> details = new ArrayList<>();
        String sql = "SELECT id.id, id.import_receipt_id, id.product_id, id.sku_id, id.quantity, " +
                     "p.name as product_name, s.code as sku_code, s.price " +
                     "FROM import_details id " +
                     "JOIN products p ON id.product_id = p.id " +
                     "JOIN skus s ON id.sku_id = s.id " +
                     "WHERE id.import_receipt_id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, receiptId);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                ImportDetailDTO detail = new ImportDetailDTO();
                detail.setId(rs.getInt("id"));
                detail.setImportReceiptId(rs.getInt("import_receipt_id"));
                detail.setProductId(rs.getInt("product_id"));
                detail.setSkuId(rs.getInt("sku_id"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setProductName(rs.getString("product_name"));
                detail.setSkuCode(rs.getString("sku_code"));
                detail.setPrice(rs.getDouble("price"));
                
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
    
    // Thêm chi tiết phiếu nhập
    public boolean AddImportDetail(int receiptId, int productId, int skuId, int quantity) {
        String sql = "INSERT INTO import_details (import_receipt_id, product_id, sku_id, quantity) VALUES (?, ?, ?, ?)";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, receiptId);
            statement.setInt(2, productId);
            statement.setInt(3, skuId);
            statement.setInt(4, quantity);
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật tồn kho SKU (tăng khi nhập)
    public boolean UpdateSkuStock(int skuId, int quantityToAdd) {
        String sql = "UPDATE skus SET stock = stock + ? WHERE id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, quantityToAdd);
            statement.setInt(2, skuId);
            
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
