package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.AttributeDTO;
import dto.AttributeOptionDTO;
import utils.DatabaseHelper;

public class AttributeDAO {
    
    // Lấy tất cả attributes
    public List<AttributeDTO> GetAllAttribute() {
        List<AttributeDTO> attributes = new ArrayList<>();
        String sql = "SELECT id, name FROM attributes ORDER BY id";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                AttributeDTO attribute = new AttributeDTO();
                attribute.setID(rs.getInt("id"));
                attribute.setName(rs.getString("name"));
                attributes.add(attribute);
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return attributes;
    }
    
    // Lấy attributes theo category id
    public List<AttributeDTO> GetAttributesByCategoryId(int categoryId) {
        List<AttributeDTO> attributes = new ArrayList<>();
        String sql = "SELECT a.id, a.name FROM attributes a " +
                     "JOIN category_attribute ca ON a.id = ca.attribute_id " +
                     "WHERE ca.category_id = ? ORDER BY a.id";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, categoryId);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                AttributeDTO attribute = new AttributeDTO();
                attribute.setID(rs.getInt("id"));
                attribute.setName(rs.getString("name"));
                attributes.add(attribute);
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return attributes;
    }
    
    // Lấy tất cả attribute options với tên thuộc tính
    public List<AttributeOptionDTO> GetAllAttributeOptions() {
        List<AttributeOptionDTO> options = new ArrayList<>();
        String sql = "SELECT ao.id, ao.attribute_id, ao.value, a.name as attribute_name " +
                     "FROM attribute_options ao " +
                     "JOIN attributes a ON ao.attribute_id = a.id " +
                     "ORDER BY ao.id ASC";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                AttributeOptionDTO option = new AttributeOptionDTO();
                option.setID(rs.getInt("id"));
                option.setAttributeId(rs.getInt("attribute_id"));
                option.setValue(rs.getString("value"));
                option.setAttributeName(rs.getString("attribute_name"));
                options.add(option);
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return options;
    }
    
    // Lấy attribute options theo attribute id
    public List<AttributeOptionDTO> GetOptionsByAttributeId(int attributeId) {
        List<AttributeOptionDTO> options = new ArrayList<>();
        String sql = "SELECT id, attribute_id, value FROM attribute_options WHERE attribute_id = ? ORDER BY value";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, attributeId);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                AttributeOptionDTO option = new AttributeOptionDTO();
                option.setID(rs.getInt("id"));
                option.setAttributeId(rs.getInt("attribute_id"));
                option.setValue(rs.getString("value"));
                options.add(option);
            }
            
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return options;
    }
    
    // Thêm attribute mới
    public boolean AddAttribute(AttributeDTO attribute) {
        String sql = "INSERT INTO attributes(name) VALUES (?)";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, attribute.getName());
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Thêm attribute option mới
    public boolean AddAttributeOption(AttributeOptionDTO option) {
        String sql = "INSERT INTO attribute_options(attribute_id, value) VALUES (?, ?)";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, option.getAttributeId());
            statement.setString(2, option.getValue());
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật attribute
    public boolean EditAttribute(AttributeDTO attribute) {
        String sql = "UPDATE attributes SET name = ? WHERE id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, attribute.getName());
            statement.setInt(2, attribute.getID());
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật attribute option
    public boolean EditAttributeOption(AttributeOptionDTO option) {
        String sql = "UPDATE attribute_options SET attribute_id = ?, value = ? WHERE id = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, option.getAttributeId());
            statement.setString(2, option.getValue());
            statement.setInt(3, option.getID());
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa attribute (cascade sẽ xóa cả options)
    public boolean DeleteAttribute(int id) {
        String sql = "DELETE FROM attributes WHERE id = ?";
        
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
    
    // Xóa attribute option
    public boolean DeleteAttributeOption(int id) {
        String sql = "DELETE FROM attribute_options WHERE id = ?";
        
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
    
    // Kiểm tra tên attribute đã tồn tại
    public boolean IsNameExists(String name) {
        String sql = "SELECT COUNT(*) FROM attributes WHERE name = ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
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
    
    // Kiểm tra tên attribute đã tồn tại (trừ id hiện tại)
    public boolean IsNameExistsExcept(String name, int exceptId) {
        String sql = "SELECT COUNT(*) FROM attributes WHERE name = ? AND id != ?";
        
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
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
}
