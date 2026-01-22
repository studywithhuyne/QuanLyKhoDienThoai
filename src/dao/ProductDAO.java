package dao;

import dto.ProductDTO;
import utils.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

	// Lấy tất cả products chưa bị xóa
	public List<ProductDTO> GetAllProduct() {
		List<ProductDTO> products = new ArrayList<>();
		String sql = "SELECT id, brand_id, category_id, name, is_deleted, created_at FROM products WHERE is_deleted = FALSE";

		try {
			Connection conn = DatabaseHelper.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				ProductDTO product = new ProductDTO();
				product.setId(rs.getInt("id"));
				product.setBrandId(rs.getInt("brand_id"));
				product.setCategoryId(rs.getInt("category_id"));
				product.setName(rs.getString("name"));
				product.setDeleted(rs.getBoolean("is_deleted"));

				Timestamp createdAt = rs.getTimestamp("created_at");
				if (createdAt != null) {
					product.setCreatedAt(createdAt.toLocalDateTime());
				}

				products.add(product);
			}

			rs.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return products;
	}

	// Lấy tất cả products (bao gồm cả đã xóa)
	public List<ProductDTO> GetAllProductIncludeDeleted() {
		List<ProductDTO> products = new ArrayList<>();
		String sql = "SELECT id, brand_id, category_id, name, is_deleted, created_at FROM products";

		try {
			Connection conn = DatabaseHelper.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				ProductDTO product = new ProductDTO();
				product.setId(rs.getInt("id"));
				product.setBrandId(rs.getInt("brand_id"));
				product.setCategoryId(rs.getInt("category_id"));
				product.setName(rs.getString("name"));
				product.setDeleted(rs.getBoolean("is_deleted"));

				Timestamp createdAt = rs.getTimestamp("created_at");
				if (createdAt != null) {
					product.setCreatedAt(createdAt.toLocalDateTime());
				}

				products.add(product);
			}

			rs.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return products;
	}

	public List<Object[]> GetProductTableData() {
		List<Object[]> rows = new ArrayList<>();
		String sql = "SELECT p.id, p.name, b.name AS brand_name, c.name AS category_name, "
				+ "COALESCE(SUM(s.stock), 0) AS total_stock "
				+ "FROM products p "
				+ "JOIN brands b ON p.brand_id = b.id "
				+ "JOIN categories c ON p.category_id = c.id "
				+ "LEFT JOIN skus s ON s.product_id = p.id "
				+ "WHERE p.is_deleted = FALSE "
				+ "GROUP BY p.id, p.name, b.name, c.name "
				+ "ORDER BY p.id";

		try {
			Connection conn = DatabaseHelper.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				Object[] row = new Object[] {
					rs.getInt("id"),
					rs.getString("name"),
					rs.getString("brand_name"),
					rs.getString("category_name"),
					rs.getInt("total_stock")
				};
				rows.add(row);
			}

			rs.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rows;
	}
	
	public boolean AddProduct(ProductDTO product) {
		
		String sql = "INSERT INTO products(brand_id, category_id, name, created_at) VALUES (?, ?, ?, ?)";
		
		try {
			Connection conn = DatabaseHelper.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, product.getBrandId());
			statement.setInt(2, product.getCategoryId());
			statement.setString(3, product.getName());
			if (product.getCreatedAt() != null) {
	            statement.setTimestamp(4, Timestamp.valueOf(product.getCreatedAt()));
	        } else {
	            // Nếu không có thời gian, lấy thời gian hiện tại
	            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
	        }
			
			int rowsAffected = statement.executeUpdate();
			return rowsAffected > 0;
		}
		catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean EditProduct(ProductDTO product) {	
		String sql = "UPDATE products "
					+ "SET brand_id = ?, category_id = ?, name = ? "
					+ "WHERE id = ? AND is_deleted = FALSE";
		try {
			Connection conn = DatabaseHelper.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setInt(1, product.getBrandId());
			statement.setInt(2, product.getCategoryId());
			statement.setString(3, product.getName());
			statement.setInt(4, product.getId());
			
			int rowsAffected = statement.executeUpdate();
			return rowsAffected > 0;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Soft delete product
	public boolean DeleteProduct(ProductDTO product) {
		String sql = "UPDATE products SET is_deleted = TRUE WHERE id = ?";
		try {
			Connection conn = DatabaseHelper.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setInt(1, product.getId());
			int rowsAffected = statement.executeUpdate();
			return rowsAffected > 0;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Khôi phục product đã xóa
	public boolean RestoreProduct(int id) {
		String sql = "UPDATE products SET is_deleted = FALSE WHERE id = ?";
		try {
			Connection conn = DatabaseHelper.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setInt(1, id);
			int rowsAffected = statement.executeUpdate();
			return rowsAffected > 0;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Lấy product theo ID
	public ProductDTO GetProductById(int id) {
		String sql = "SELECT id, brand_id, category_id, name, is_deleted, created_at FROM products WHERE id = ?";
		
		try {
			Connection conn = DatabaseHelper.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			
			if (rs.next()) {
				ProductDTO product = new ProductDTO();
				product.setId(rs.getInt("id"));
				product.setBrandId(rs.getInt("brand_id"));
				product.setCategoryId(rs.getInt("category_id"));
				product.setName(rs.getString("name"));
				product.setDeleted(rs.getBoolean("is_deleted"));

				Timestamp createdAt = rs.getTimestamp("created_at");
				if (createdAt != null) {
					product.setCreatedAt(createdAt.toLocalDateTime());
				}
				
				rs.close();
				statement.close();
				return product;
			}
			
			rs.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
