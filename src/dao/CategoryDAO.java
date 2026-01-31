package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.CategoryDTO;
import utils.DatabaseHelper;

public class CategoryDAO {
	
	// Lấy tất cả categories chưa bị xóa
	public List<CategoryDTO> GetAllCategory() {
		List<CategoryDTO> categories = new ArrayList<>();
		String sql = "SELECT id, name, is_deleted FROM categories WHERE is_deleted = FALSE";
		
		try {
			Connection conn = DatabaseHelper.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			
			while (rs.next()) {
				CategoryDTO category = new CategoryDTO();
				category.setID(rs.getInt("id"));
				category.setName(rs.getString("name"));
				category.setDeleted(rs.getBoolean("is_deleted"));
				
				categories.add(category);
			}
			
			rs.close();
			statement.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return categories;
	}
	
	// Lấy tất cả categories (bao gồm cả đã xóa)
	public List<CategoryDTO> GetAllCategoryIncludeDeleted() {
		List<CategoryDTO> categories = new ArrayList<>();
		String sql = "SELECT id, name, is_deleted FROM categories";
		
		try {
			Connection conn = DatabaseHelper.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			
			while (rs.next()) {
				CategoryDTO category = new CategoryDTO();
				category.setID(rs.getInt("id"));
				category.setName(rs.getString("name"));
				category.setDeleted(rs.getBoolean("is_deleted"));
				
				categories.add(category);
			}
			
			rs.close();
			statement.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return categories;
	}
	
	// Thêm category mới
	public boolean AddCategory(CategoryDTO category) {
		String sql = "INSERT INTO categories(name) VALUES (?)";
		
		try {
			Connection conn = DatabaseHelper.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, category.getName());
			
			int rowsAffected = statement.executeUpdate();
			statement.close();
			return rowsAffected > 0;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Cập nhật category
	public boolean EditCategory(CategoryDTO category) {
		String sql = "UPDATE categories SET name = ? WHERE id = ? AND is_deleted = FALSE";
		
		try {
			Connection conn = DatabaseHelper.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, category.getName());
			statement.setInt(2, category.getID());
			
			int rowsAffected = statement.executeUpdate();
			statement.close();
			return rowsAffected > 0;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Soft delete category
	public boolean DeleteCategory(int id) {
		String sql = "UPDATE categories SET is_deleted = TRUE WHERE id = ?";
		
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
	
	// Lấy category theo ID
	public CategoryDTO GetCategoryById(int id) {
		String sql = "SELECT id, name, is_deleted FROM categories WHERE id = ?";
		
		try {
			Connection conn = DatabaseHelper.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			
			if (rs.next()) {
				CategoryDTO category = new CategoryDTO();
				category.setID(rs.getInt("id"));
				category.setName(rs.getString("name"));
				category.setDeleted(rs.getBoolean("is_deleted"));
				
				rs.close();
				statement.close();
				return category;
			}
			
			rs.close();
			statement.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
