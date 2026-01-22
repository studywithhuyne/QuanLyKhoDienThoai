package ui.category;

import javax.swing.*;
import java.util.List;

import dao.CategoryDAO;
import dto.CategoryDTO;
import ui.BaseCrudPanel;

/**
 * Panel quản lý Danh mục sản phẩm
 */
public class CategoryPanel extends BaseCrudPanel {
    
    private static final String[] COLUMNS = {"ID", "Tên danh mục"};
    
    public CategoryPanel(JFrame parentFrame) {
        super(parentFrame, "danh mục", COLUMNS);
    }
    
    @Override
    public void loadData() {
        CategoryDAO categoryDAO = new CategoryDAO();
        List<CategoryDTO> categories = categoryDAO.GetAllCategory();
        tableModel.setRowCount(0);
        for (CategoryDTO category : categories) {
            tableModel.addRow(new Object[]{category.getID(), category.getName()});
        }
    }
    
    @Override
    protected void setupColumnWidths() {
        setFixedColumnWidth(0, 80);   // ID
        setFlexibleColumnWidth(1, 200, 400); // Tên danh mục
    }
    
    @Override
    protected void onAddAction() {
        new CategoryAddDialog(parentFrame, this);
    }
    
    @Override
    protected void onEditAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String name = (String) getValueAt(modelRow, 1);
        new CategoryEditDialog(parentFrame, id, name, this);
    }
    
    @Override
    protected void onDeleteAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String name = (String) getValueAt(modelRow, 1);
        new CategoryDeleteDialog(parentFrame, id, name, this);
    }
}
