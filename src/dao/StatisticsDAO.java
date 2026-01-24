package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

import utils.DatabaseHelper;

public class StatisticsDAO {
    
    // Tổng tiền nhập hàng theo tháng/năm
    public double GetTotalImport(int month, int year) {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) as total FROM import_receipts " +
                     "WHERE MONTH(created_at) = ? AND YEAR(created_at) = ?";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double total = rs.getDouble("total");
                rs.close();
                stmt.close();
                return total;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Số phiếu nhập theo tháng/năm
    public int GetImportCount(int month, int year) {
        String sql = "SELECT COUNT(*) as cnt FROM import_receipts " +
                     "WHERE MONTH(created_at) = ? AND YEAR(created_at) = ?";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("cnt");
                rs.close();
                stmt.close();
                return count;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Tổng tiền xuất hàng theo tháng/năm
    public double GetTotalSales(int month, int year) {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) as total FROM invoices " +
                     "WHERE MONTH(created_at) = ? AND YEAR(created_at) = ?";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double total = rs.getDouble("total");
                rs.close();
                stmt.close();
                return total;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Số hóa đơn bán theo tháng/năm
    public int GetSalesCount(int month, int year) {
        String sql = "SELECT COUNT(*) as cnt FROM invoices " +
                     "WHERE MONTH(created_at) = ? AND YEAR(created_at) = ?";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("cnt");
                rs.close();
                stmt.close();
                return count;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Top 5 sản phẩm bán chạy theo tháng/năm
    public Map<String, Integer> GetTop5BestSellers(int month, int year) {
        Map<String, Integer> data = new LinkedHashMap<>();
        String sql = "SELECT p.name, SUM(id.quantity) as total_sold " +
                     "FROM invoice_details id " +
                     "JOIN invoices i ON id.invoice_id = i.id " +
                     "JOIN skus s ON id.sku_id = s.id " +
                     "JOIN products p ON s.product_id = p.id " +
                     "WHERE MONTH(i.created_at) = ? AND YEAR(i.created_at) = ? " +
                     "GROUP BY p.id, p.name " +
                     "ORDER BY total_sold DESC " +
                     "LIMIT 5";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                data.put(rs.getString("name"), rs.getInt("total_sold"));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    
    // Tổng tiền nhập hàng tháng này
    public double GetTotalImportThisMonth() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) as total FROM import_receipts " +
                     "WHERE MONTH(created_at) = MONTH(CURRENT_DATE()) " +
                     "AND YEAR(created_at) = YEAR(CURRENT_DATE())";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double total = rs.getDouble("total");
                rs.close();
                stmt.close();
                return total;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Số phiếu nhập tháng này
    public int GetImportCountThisMonth() {
        String sql = "SELECT COUNT(*) as cnt FROM import_receipts " +
                     "WHERE MONTH(created_at) = MONTH(CURRENT_DATE()) " +
                     "AND YEAR(created_at) = YEAR(CURRENT_DATE())";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("cnt");
                rs.close();
                stmt.close();
                return count;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Tổng tiền xuất hàng tháng này
    public double GetTotalSalesThisMonth() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) as total FROM invoices " +
                     "WHERE MONTH(created_at) = MONTH(CURRENT_DATE()) " +
                     "AND YEAR(created_at) = YEAR(CURRENT_DATE())";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double total = rs.getDouble("total");
                rs.close();
                stmt.close();
                return total;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Số hóa đơn bán tháng này
    public int GetSalesCountThisMonth() {
        String sql = "SELECT COUNT(*) as cnt FROM invoices " +
                     "WHERE MONTH(created_at) = MONTH(CURRENT_DATE()) " +
                     "AND YEAR(created_at) = YEAR(CURRENT_DATE())";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("cnt");
                rs.close();
                stmt.close();
                return count;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Tổng giá trị tồn kho (stock * price)
    public double GetTotalInventoryValue() {
        String sql = "SELECT COALESCE(SUM(stock * price), 0) as total FROM skus";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double total = rs.getDouble("total");
                rs.close();
                stmt.close();
                return total;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Tổng số SKU có trong kho (stock > 0)
    public int GetTotalSkuInStock() {
        String sql = "SELECT COUNT(*) as cnt FROM skus WHERE stock > 0";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("cnt");
                rs.close();
                stmt.close();
                return count;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Thống kê nhập hàng 6 tháng gần nhất
    public Map<String, Double> GetImportLast6Months() {
        Map<String, Double> data = new LinkedHashMap<>();
        String sql = "SELECT DATE_FORMAT(created_at, '%m/%Y') as month, SUM(total_amount) as total " +
                     "FROM import_receipts " +
                     "WHERE created_at >= DATE_SUB(CURRENT_DATE(), INTERVAL 6 MONTH) " +
                     "GROUP BY DATE_FORMAT(created_at, '%Y-%m') " +
                     "ORDER BY DATE_FORMAT(created_at, '%Y-%m')";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                data.put(rs.getString("month"), rs.getDouble("total"));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    
    // Thống kê xuất hàng 6 tháng gần nhất
    public Map<String, Double> GetSalesLast6Months() {
        Map<String, Double> data = new LinkedHashMap<>();
        String sql = "SELECT DATE_FORMAT(created_at, '%m/%Y') as month, SUM(total_amount) as total " +
                     "FROM invoices " +
                     "WHERE created_at >= DATE_SUB(CURRENT_DATE(), INTERVAL 6 MONTH) " +
                     "GROUP BY DATE_FORMAT(created_at, '%Y-%m') " +
                     "ORDER BY DATE_FORMAT(created_at, '%Y-%m')";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                data.put(rs.getString("month"), rs.getDouble("total"));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    
    // Thống kê tồn kho theo danh mục
    public Map<String, Integer> GetStockByCategory() {
        Map<String, Integer> data = new LinkedHashMap<>();
        String sql = "SELECT c.name, COALESCE(SUM(s.stock), 0) as total_stock " +
                     "FROM categories c " +
                     "LEFT JOIN products p ON c.id = p.category_id AND p.is_deleted = FALSE " +
                     "LEFT JOIN skus s ON p.id = s.product_id " +
                     "WHERE c.is_deleted = FALSE " +
                     "GROUP BY c.id, c.name " +
                     "ORDER BY total_stock DESC";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                data.put(rs.getString("name"), rs.getInt("total_stock"));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    
    // Top 5 sản phẩm bán chạy
    public Map<String, Integer> GetTop5BestSellers() {
        Map<String, Integer> data = new LinkedHashMap<>();
        String sql = "SELECT p.name, SUM(id.quantity) as total_sold " +
                     "FROM invoice_details id " +
                     "JOIN skus s ON id.sku_id = s.id " +
                     "JOIN products p ON s.product_id = p.id " +
                     "GROUP BY p.id, p.name " +
                     "ORDER BY total_sold DESC " +
                     "LIMIT 5";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                data.put(rs.getString("name"), rs.getInt("total_sold"));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    
    // Sản phẩm sắp hết hàng (stock <= 5)
    public Map<String, Integer> GetLowStockProducts() {
        Map<String, Integer> data = new LinkedHashMap<>();
        String sql = "SELECT p.name, s.code, s.stock " +
                     "FROM skus s " +
                     "JOIN products p ON s.product_id = p.id " +
                     "WHERE s.stock > 0 AND s.stock <= 5 " +
                     "ORDER BY s.stock ASC " +
                     "LIMIT 10";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name") + " (" + rs.getString("code") + ")";
                data.put(name, rs.getInt("stock"));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
