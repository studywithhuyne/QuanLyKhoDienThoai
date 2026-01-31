package bus;

import java.util.List;
import dao.CategoryDAO;
import dto.CategoryDTO;

public class CategoryBUS {
    
    private CategoryDAO categoryDAO;
    
    public CategoryBUS() {
        this.categoryDAO = new CategoryDAO();
    }
    
    public List<CategoryDTO> getAll() {
        return categoryDAO.GetAllCategory();
    }
    
    public List<CategoryDTO> getAllIncludeDeleted() {
        return categoryDAO.GetAllCategoryIncludeDeleted();
    }
    
    public CategoryDTO getById(int id) {
        return categoryDAO.GetCategoryById(id);
    }
    
    public boolean add(CategoryDTO category) {
        if (!validateCategory(category)) return false;
        return categoryDAO.AddCategory(category);
    }
    
    public boolean update(CategoryDTO category) {
        if (category.getID() <= 0) return false;
        if (!validateCategory(category)) return false;
        return categoryDAO.EditCategory(category);
    }
    
    public boolean delete(int id) {
        if (id <= 0) return false;
        return categoryDAO.DeleteCategory(id);
    }
    
    private boolean validateCategory(CategoryDTO category) {
        if (category == null) return false;
        if (category.getName() == null || category.getName().trim().isEmpty()) return false;
        return true;
    }
    

}
