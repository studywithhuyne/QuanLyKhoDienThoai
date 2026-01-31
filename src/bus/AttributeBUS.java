package bus;

import java.util.List;
import dao.AttributeDAO;
import dto.AttributeDTO;
import dto.AttributeOptionDTO;

public class AttributeBUS {
    
    private AttributeDAO attributeDAO;
    
    public AttributeBUS() {
        this.attributeDAO = new AttributeDAO();
    }
    
    public List<AttributeDTO> getAll() {
        return attributeDAO.GetAllAttribute();
    }
    
    public List<AttributeDTO> getByCategoryId(int categoryId) {
        return attributeDAO.GetAttributesByCategoryId(categoryId);
    }
    
    public boolean add(AttributeDTO attribute) {
        if (!validateAttribute(attribute)) return false;
        return attributeDAO.AddAttribute(attribute);
    }
    
    public boolean update(AttributeDTO attribute) {
        if (attribute.getID() <= 0) return false;
        if (!validateAttribute(attribute)) return false;
        return attributeDAO.EditAttribute(attribute);
    }
    
    public boolean delete(int id) {
        if (id <= 0) return false;
        return attributeDAO.DeleteAttribute(id);
    }
    
    public List<AttributeOptionDTO> getAllOptions() {
        return attributeDAO.GetAllAttributeOptions();
    }
    
    public List<AttributeOptionDTO> getOptionsByAttributeId(int attributeId) {
        return attributeDAO.GetOptionsByAttributeId(attributeId);
    }
    
    public boolean addOption(AttributeOptionDTO option) {
        if (!validateAttributeOption(option)) return false;
        return attributeDAO.AddAttributeOption(option);
    }
    
    public boolean updateOption(AttributeOptionDTO option) {
        if (option.getID() <= 0) return false;
        if (!validateAttributeOption(option)) return false;
        return attributeDAO.EditAttributeOption(option);
    }
    
    public boolean deleteOption(int id) {
        if (id <= 0) return false;
        return attributeDAO.DeleteAttributeOption(id);
    }
    
    private boolean validateAttribute(AttributeDTO attribute) {
        if (attribute == null) return false;
        if (attribute.getName() == null || attribute.getName().trim().isEmpty()) return false;
        return true;
    }
    
    private boolean validateAttributeOption(AttributeOptionDTO option) {
        if (option == null) return false;
        if (option.getAttributeId() <= 0) return false;
        if (option.getValue() == null || option.getValue().trim().isEmpty()) return false;
        return true;
    }
    
    public boolean isNameExists(String name) {
        return attributeDAO.IsNameExists(name);
    }
    
    public boolean isNameExistsExcept(String name, int excludeId) {
        return attributeDAO.IsNameExistsExcept(name, excludeId);
    }
    

}
