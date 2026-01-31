package bus;

import java.util.List;
import dao.SkuDAO;
import dto.SkuDTO;

public class SkuBUS {
    
    private SkuDAO skuDAO;
    
    public SkuBUS() {
        this.skuDAO = new SkuDAO();
    }
    
    public List<SkuDTO> getAll() {
        return skuDAO.GetAllSku();
    }
    

    public List<SkuDTO> getByProductId(int productId) {
        return skuDAO.GetSkusByProductId(productId);
    }
    
    public int add(SkuDTO sku) {
        if (!validateSku(sku)) return -1;
        return skuDAO.AddSku(sku);
    }
    
    public boolean addAttributeOption(int skuId, int attributeOptionId) {
        if (skuId <= 0 || attributeOptionId <= 0) return false;
        return skuDAO.AddSkuAttributeOption(skuId, attributeOptionId);
    }
    
    public boolean update(SkuDTO sku) {
        if (sku.getID() <= 0) return false;
        if (!validateSku(sku)) return false;
        return skuDAO.EditSku(sku);
    }
    
    public boolean delete(int id) {
        if (id <= 0) return false;
        return skuDAO.DeleteSku(id);
    }
    
    public boolean deleteAttributeOptions(int skuId) {
        if (skuId <= 0) return false;
        return skuDAO.DeleteSkuAttributeOptions(skuId);
    }
    
    private boolean validateSku(SkuDTO sku) {
        if (sku == null) return false;
        if (sku.getProductId() <= 0) return false;
        if (sku.getCode() == null || sku.getCode().trim().isEmpty()) return false;
        if (sku.getPrice() < 0) return false;
        if (sku.getStock() < 0) return false;
        return true;
    }
    
    public boolean isCodeExists(String code) {
        return skuDAO.IsCodeExists(code);
    }
    
    public boolean isCodeExistsExcept(String code, int excludeId) {
        return skuDAO.IsCodeExistsExcept(code, excludeId);
    }
    
    public boolean updateStock(int skuId, int newStock) {
        if (skuId <= 0 || newStock < 0) return false;
        return skuDAO.UpdateStock(skuId, newStock);
    }
    
    public boolean increaseStock(int skuId, int quantity) {
        if (skuId <= 0 || quantity <= 0) return false;
        return skuDAO.UpdateStock(skuId, quantity);
    }
    
    public boolean decreaseStock(int skuId, int quantity) {
        if (skuId <= 0 || quantity <= 0) return false;
        return skuDAO.UpdateStock(skuId, -quantity);
    }
    
    public List<Integer> getAttributeOptionIds(int skuId) {
        return skuDAO.GetSkuAttributeOptionIds(skuId);
    }
}
