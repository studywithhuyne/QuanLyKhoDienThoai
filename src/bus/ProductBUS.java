package bus;

import java.util.List;
import dao.ProductDAO;
import dto.ProductDTO;

public class ProductBUS {
    
    private ProductDAO productDAO;
    
    public ProductBUS() {
        this.productDAO = new ProductDAO();
    }
    
    public List<ProductDTO> getAll() {
        return productDAO.GetAllProduct();
    }
    
    public List<ProductDTO> getAllIncludeDeleted() {
        return productDAO.GetAllProductIncludeDeleted();
    }
    
    public List<Object[]> getTableData() {
        return productDAO.GetProductTableData();
    }
    
    public ProductDTO getById(int id) {
        return productDAO.GetProductById(id);
    }
    
    public boolean add(ProductDTO product) {
        if (!validateProduct(product)) return false;
        return productDAO.AddProduct(product);
    }
    
    public int addAndGetId(ProductDTO product) {
        if (!validateProduct(product)) return -1;
        return productDAO.AddProductReturnId(product);
    }
    
    public boolean update(ProductDTO product) {
        if (product.getId() <= 0) return false;
        if (!validateProduct(product)) return false;
        return productDAO.EditProduct(product);
    }
    
    public boolean delete(int id) {
        if (id <= 0) return false;
        ProductDTO product = productDAO.GetProductById(id);
        if (product == null) return false;
        return productDAO.DeleteProduct(product);
    }
    
    private boolean validateProduct(ProductDTO product) {
        if (product == null) return false;
        if (product.getName() == null || product.getName().trim().isEmpty()) return false;
        if (product.getBrandId() <= 0) return false;
        if (product.getCategoryId() <= 0) return false;
        return true;
    }
    

}
