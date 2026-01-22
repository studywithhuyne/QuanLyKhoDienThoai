package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.BrandDTO;
import utils.DatabaseHelper;

public class BrandDAO {
	
	// Lấy tất cả brands chưa bị xóa
	public List<BrandDTO> GetAllBrand() {
		List<BrandDTO> brands = new ArrayList<>();
		String sql = "SELECT id, name, is_deleted FROM brands WHERE is_deleted = FALSE";
		
		try {
			Connection conn = DatabaseHelper.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			
			while (rs.next()) {
				BrandDTO brand = new BrandDTO();
				brand.setID(rs.getInt("id"));
				brand.setName(rs.getString("name"));
				brand.setDeleted(rs.getBoolean("is_deleted"));
				
				brands.add(brand);
			}
			
			rs.close();
			statement.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return brands;
	}
	
	// Lấy tất cả brands (bao gồm cả đã xóa)
	public List<BrandDTO> GetAllBrandIncludeDeleted() {
		List<BrandDTO> brands = new ArrayList<>();
		String sql = "SELECT id, name, is_deleted FROM brands";
		
		try {
			Connection conn = DatabaseHelper.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			
			while (rs.next()) {
				BrandDTO brand = new BrandDTO();
				brand.setID(rs.getInt("id"));
				brand.setName(rs.getString("name"));
				brand.setDeleted(rs.getBoolean("is_deleted"));
				
				brands.add(brand);
			}
			
			rs.close();
			statement.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return brands;
	}
	
	// Thêm brand mới
	public boolean AddBrand(BrandDTO brand) {
		String sql = "INSERT INTO brands(name) VALUES (?)";
		
		try {
			Connection conn = DatabaseHelper.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, brand.getName());
			
			int rowsAffected = statement.executeUpdate();
			statement.close();
			return rowsAffected > 0;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Cập nhật brand
	public boolean EditBrand(BrandDTO brand) {
		String sql = "UPDATE brands SET name = ? WHERE id = ? AND is_deleted = FALSE";
		
		try {
			Connection conn = DatabaseHelper.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, brand.getName());
			statement.setInt(2, brand.getID());
			
			int rowsAffected = statement.executeUpdate();
			statement.close();
			return rowsAffected > 0;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Soft delete brand
	public boolean DeleteBrand(int id) {
		String sql = "UPDATE brands SET is_deleted = TRUE WHERE id = ?";
		
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
	
	// Khôi phục brand đã xóa
	public boolean RestoreBrand(int id) {
		String sql = "UPDATE brands SET is_deleted = FALSE WHERE id = ?";
		
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
	
	// Lấy brand theo ID
	public BrandDTO GetBrandById(int id) {
		String sql = "SELECT id, name, is_deleted FROM brands WHERE id = ?";
		
		try {
			Connection conn = DatabaseHelper.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			
			if (rs.next()) {
				BrandDTO brand = new BrandDTO();
				brand.setID(rs.getInt("id"));
				brand.setName(rs.getString("name"));
				brand.setDeleted(rs.getBoolean("is_deleted"));
				
				rs.close();
				statement.close();
				return brand;
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
