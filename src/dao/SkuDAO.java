package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.SkuDTO;
import utils.DatabaseHelper;

public class SkuDAO {
    
    // Lấy tất cả SKUs với thông tin sản phẩm và thuộc tính
    public List<SkuDTO> GetAllSku() {
        List<SkuDTO> skus = new ArrayList<>();
        String sql = "SELECT s.id, s.product_id, s.code, s.price, s.stock, p.name as product_name " +
                     "FROM skus s " +
                     "JOIN products p ON s.product_id = p.id " +
                     "WHERE p.is_deleted = FALSE " +
                     "ORDER BY s.id";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                SkuDTO sku = new SkuDTO();
                sku.setID(rs.getInt("id"));
                sku.setProductId(rs.getInt("product_id"));
                sku.setCode(rs.getString("code"));
                sku.setPrice(rs.getDouble("price"));
                sku.setStock(rs.getInt("stock"));
                sku.setProductName(rs.getString("product_name"));
                
                // Lấy thuộc tính của SKU
                sku.setAttributes(getSkuAttributes(sku.getID()));
                
                skus.add(sku);
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return skus;
    }
    
    // Lấy thuộc tính của SKU
    private String getSkuAttributes(int skuId) {
        StringBuilder attributes = new StringBuilder();
        String sql = "SELECT ao.value " +
                     "FROM attribute_option_sku aos " +
                     "JOIN attribute_options ao ON aos.attribute_option_id = ao.id " +
                     "WHERE aos.sku_id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, skuId);
            ResultSet rs = statement.executeQuery();
            
            boolean first = true;
            while (rs.next()) {
                if (!first) {
                    attributes.append(", ");
                }
                attributes.append(rs.getString("value"));
                first = false;
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return attributes.toString();
    }
    
    // Lấy SKUs theo product id
    public List<SkuDTO> GetSkusByProductId(int productId) {
        List<SkuDTO> skus = new ArrayList<>();
        String sql = "SELECT id, product_id, code, price, stock FROM skus WHERE product_id = ? ORDER BY id";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, productId);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                SkuDTO sku = new SkuDTO();
                sku.setID(rs.getInt("id"));
                sku.setProductId(rs.getInt("product_id"));
                sku.setCode(rs.getString("code"));
                sku.setPrice(rs.getDouble("price"));
                sku.setStock(rs.getInt("stock"));
                sku.setAttributes(getSkuAttributes(sku.getID()));
                skus.add(sku);
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return skus;
    }
    
    // Thêm SKU mới
    public int AddSku(SkuDTO sku) {
        String sql = "INSERT INTO skus(product_id, code, price, stock) VALUES (?, ?, ?, ?)";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, sku.getProductId());
            statement.setString(2, sku.getCode());
            statement.setDouble(3, sku.getPrice());
            statement.setInt(4, sku.getStock());
            
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
    
    // Thêm attribute option cho SKU
    public boolean AddSkuAttributeOption(int skuId, int attributeOptionId) {
        String sql = "INSERT INTO attribute_option_sku(sku_id, attribute_option_id) VALUES (?, ?)";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, skuId);
            statement.setInt(2, attributeOptionId);
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật SKU
    public boolean EditSku(SkuDTO sku) {
        String sql = "UPDATE skus SET product_id = ?, code = ?, price = ?, stock = ? WHERE id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, sku.getProductId());
            statement.setString(2, sku.getCode());
            statement.setDouble(3, sku.getPrice());
            statement.setInt(4, sku.getStock());
            statement.setInt(5, sku.getID());
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa tất cả attribute options của SKU
    public boolean DeleteSkuAttributeOptions(int skuId) {
        String sql = "DELETE FROM attribute_option_sku WHERE sku_id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, skuId);
            
            statement.executeUpdate();
            statement.close();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Lấy danh sách attribute option ids của SKU
    public List<Integer> GetSkuAttributeOptionIds(int skuId) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT attribute_option_id FROM attribute_option_sku WHERE sku_id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, skuId);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                ids.add(rs.getInt("attribute_option_id"));
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }
    
    // Xóa SKU (cascade sẽ xóa attribute_option_sku)
    public boolean DeleteSku(int id) {
        String sql = "DELETE FROM skus WHERE id = ?";
        
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
    
    // Kiểm tra mã SKU đã tồn tại
    public boolean IsCodeExists(String code) {
        String sql = "SELECT COUNT(*) FROM skus WHERE code = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, code);
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
    
    // Kiểm tra mã SKU đã tồn tại (trừ id hiện tại)
    public boolean IsCodeExistsExcept(String code, int exceptId) {
        String sql = "SELECT COUNT(*) FROM skus WHERE code = ? AND id != ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, code);
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
    
    // Cập nhật tồn kho
    public boolean UpdateStock(int skuId, int quantity) {
        String sql = "UPDATE skus SET stock = stock + ? WHERE id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, quantity);
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
