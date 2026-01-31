package ui.product;

import javax.swing.*;
import java.util.List;

import bus.ProductBUS;
import ui.BaseCrudPanel;

/**
 * Panel quản lý Sản phẩm
 */
public class ProductPanel extends BaseCrudPanel {
    
    private static final String[] COLUMNS = {"ID", "Tên sản phẩm", "Thương hiệu", "Danh mục", "Tồn kho"};
    private ProductBUS productBUS = new ProductBUS();
    
    public ProductPanel(JFrame parentFrame) {
        super(parentFrame, "sản phẩm", COLUMNS);
    }
    
    @Override
    public void loadData() {
        List<Object[]> rows = productBUS.getTableData();
        tableModel.setRowCount(0);
        for (Object[] row : rows) {
            tableModel.addRow(row);
        }
    }
    
    @Override
    protected void setupColumnWidths() {
        setFixedColumnWidth(0, 50);           // ID
        setFlexibleColumnWidth(1, 200, 400);  // Tên sản phẩm
        setRangeColumnWidth(2, 100, 150, 120); // Thương hiệu
        setRangeColumnWidth(3, 100, 150, 120); // Danh mục
        setFixedColumnWidth(4, 80);           // Tồn kho
    }
    
    @Override
    protected void onAddAction() {
        new ProductAddDialog(parentFrame, this);
    }
    
    @Override
    protected void onEditAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String name = (String) getValueAt(modelRow, 1);
        String brand = (String) getValueAt(modelRow, 2);
        String category = (String) getValueAt(modelRow, 3);
        new ProductEditDialog(parentFrame, id, name, brand, category, this);
    }
    
    @Override
    protected void onDeleteAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String name = (String) getValueAt(modelRow, 1);
        new ProductDeleteDialog(parentFrame, id, name, this);
    }
}
