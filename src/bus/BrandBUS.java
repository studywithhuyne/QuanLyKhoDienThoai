package bus;

import java.util.List;
import dao.BrandDAO;
import dto.BrandDTO;

public class BrandBUS {
    
    private BrandDAO brandDAO;
    
    public BrandBUS() {
        this.brandDAO = new BrandDAO();
    }
    
    public List<BrandDTO> getAll() {
        return brandDAO.GetAllBrand();
    }
    
    public List<BrandDTO> getAllIncludeDeleted() {
        return brandDAO.GetAllBrandIncludeDeleted();
    }
    
    public BrandDTO getById(int id) {
        return brandDAO.GetBrandById(id);
    }
    
    public boolean add(BrandDTO brand) {
        if (!validateBrand(brand)) return false;
        return brandDAO.AddBrand(brand);
    }
    
    public boolean update(BrandDTO brand) {
        if (brand.getID() <= 0) return false;
        if (!validateBrand(brand)) return false;
        return brandDAO.EditBrand(brand);
    }
    
    public boolean delete(int id) {
        if (id <= 0) return false;
        return brandDAO.DeleteBrand(id);
    }
    
    private boolean validateBrand(BrandDTO brand) {
        if (brand == null) return false;
        if (brand.getName() == null || brand.getName().trim().isEmpty()) return false;
        return true;
    }
    

}
