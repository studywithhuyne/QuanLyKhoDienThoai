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

	public List<ProductDTO> GetAllProduct() {
		List<ProductDTO> products = new ArrayList<>();
		String sql = "SELECT id, brand_id, category_id, name, created_at FROM products";

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
}
