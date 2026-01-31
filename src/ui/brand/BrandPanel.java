package ui.brand;

import javax.swing.*;
import java.util.List;

import bus.BrandBUS;
import dto.BrandDTO;
import ui.BaseCrudPanel;

/**
 * Panel quản lý Thương hiệu (Hãng sản phẩm)
 */
public class BrandPanel extends BaseCrudPanel {
    
    private static final String[] COLUMNS = {"ID", "Tên thương hiệu"};
    private BrandBUS brandBUS = new BrandBUS();
    
    public BrandPanel(JFrame parentFrame) {
        super(parentFrame, "thương hiệu", COLUMNS);
    }
    
    @Override
    public void loadData() {
        List<BrandDTO> brands = brandBUS.getAll();
        tableModel.setRowCount(0);
        for (BrandDTO brand : brands) {
            tableModel.addRow(new Object[]{brand.getID(), brand.getName()});
        }
    }
    
    @Override
    protected void setupColumnWidths() {
        setFixedColumnWidth(0, 80);   // ID
        setFlexibleColumnWidth(1, 200, 400); // Tên thương hiệu
    }
    
    @Override
    protected void onAddAction() {
        new BrandAddDialog(parentFrame, this);
    }
    
    @Override
    protected void onEditAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String name = (String) getValueAt(modelRow, 1);
        new BrandEditDialog(parentFrame, id, name, this);
    }
    
    @Override
    protected void onDeleteAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String name = (String) getValueAt(modelRow, 1);
        new BrandDeleteDialog(parentFrame, id, name, this);
    }
}
