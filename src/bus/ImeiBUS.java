package bus;

import java.util.List;
import dao.ImeiDAO;
import dto.ImeiDTO;

public class ImeiBUS {
    
    private ImeiDAO imeiDAO;
    
    public ImeiBUS() {
        this.imeiDAO = new ImeiDAO();
    }
    
    public List<ImeiDTO> getAll() {
        return imeiDAO.GetAllImei();
    }
    
    public List<ImeiDTO> getBySkuId(int skuId) {
        return imeiDAO.GetImeisBySkuId(skuId);
    }
    
    public List<ImeiDTO> getAvailableBySkuId(int skuId) {
        return imeiDAO.GetAvailableImeisBySkuId(skuId);
    }
    
    public boolean add(ImeiDTO imei) {
        if (!validateImei(imei)) return false;
        return imeiDAO.AddImei(imei);
    }
    
    public boolean update(ImeiDTO imei) {
        if (imei.getID() <= 0) return false;
        if (!validateImei(imei)) return false;
        return imeiDAO.EditImei(imei);
    }
    
    public boolean delete(int id) {
        if (id <= 0) return false;
        return imeiDAO.DeleteImei(id);
    }
    
    private boolean validateImei(ImeiDTO imei) {
        if (imei == null) return false;
        if (imei.getSkuId() <= 0) return false;
        if (imei.getImei() == null || imei.getImei().trim().isEmpty()) return false;
        if (!imei.getImei().matches("^\\d{15}$")) return false;
        return true;
    }
    
    public boolean isImeiExists(String imei) {
        return imeiDAO.IsImeiExists(imei);
    }
    
    public boolean isImeiExistsExcept(String imei, int excludeId) {
        return imeiDAO.IsImeiExistsExcept(imei, excludeId);
    }
    
    public boolean updateStatus(int id, String status) {
        if (id <= 0 || status == null || status.trim().isEmpty()) return false;
        if (!status.equals("available") && !status.equals("sold")) return false;
        return imeiDAO.UpdateStatus(id, status);
    }
    

}
