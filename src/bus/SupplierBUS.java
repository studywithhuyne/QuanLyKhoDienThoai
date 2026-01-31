package bus;

import java.util.List;
import dao.SupplierDAO;
import dto.SupplierDTO;

public class SupplierBUS {
    
    private SupplierDAO supplierDAO;
    
    public SupplierBUS() {
        this.supplierDAO = new SupplierDAO();
    }
    
    public List<SupplierDTO> getAll() {
        return supplierDAO.GetAllSupplier();
    }
    
    public boolean add(SupplierDTO supplier) {
        if (!validateSupplier(supplier)) return false;
        return supplierDAO.AddSupplier(supplier);
    }
    
    public boolean update(SupplierDTO supplier) {
        if (supplier.getID() <= 0) return false;
        if (!validateSupplier(supplier)) return false;
        return supplierDAO.EditSupplier(supplier);
    }
    
    public boolean delete(int id) {
        if (id <= 0) return false;
        return supplierDAO.DeleteSupplier(id);
    }
    
    private boolean validateSupplier(SupplierDTO supplier) {
        if (supplier == null) return false;
        if (supplier.getName() == null || supplier.getName().trim().isEmpty()) return false;
        return true;
    }
    
    public boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return true;
        return phone.matches("^[+\\d\\s-]{9,15}$");
    }
    
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return true;
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }
    
    public boolean isNameExists(String name) {
        return supplierDAO.IsNameExists(name);
    }
    
    public boolean isNameExistsExcept(String name, int excludeId) {
        return supplierDAO.IsNameExistsExcept(name, excludeId);
    }
}
