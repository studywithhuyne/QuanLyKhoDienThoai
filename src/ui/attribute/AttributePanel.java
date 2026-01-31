package ui.attribute;

import javax.swing.*;
import java.util.List;

import bus.AttributeBUS;
import dto.AttributeOptionDTO;
import ui.BaseCrudPanel;

/**
 * Panel quản lý Thuộc tính sản phẩm
 */
public class AttributePanel extends BaseCrudPanel {
    
    private static final String[] COLUMNS = {"ID", "Tên thuộc tính", "Giá trị"};
    private AttributeBUS attributeBUS = new AttributeBUS();
    
    public AttributePanel(JFrame parentFrame) {
        super(parentFrame, "thuộc tính", COLUMNS);
    }
    
    @Override
    public void loadData() {
        List<AttributeOptionDTO> options = attributeBUS.getAllOptions();
        tableModel.setRowCount(0);
        for (AttributeOptionDTO option : options) {
            tableModel.addRow(new Object[]{
                option.getID(), 
                option.getAttributeName(), 
                option.getValue()
            });
        }
    }
    
    @Override
    protected void setupColumnWidths() {
        setFixedColumnWidth(0, 80);   // ID
        setFlexibleColumnWidth(1, 150, 250); // Tên thuộc tính
        setFlexibleColumnWidth(2, 150, 300); // Giá trị
    }
    
    @Override
    protected void onAddAction() {
        new AttributeAddDialog(parentFrame, this);
    }
    
    @Override
    protected void onEditAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String attributeName = (String) getValueAt(modelRow, 1);
        String value = (String) getValueAt(modelRow, 2);
        new AttributeEditDialog(parentFrame, id, attributeName, value, this);
    }
    
    @Override
    protected void onDeleteAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String attributeName = (String) getValueAt(modelRow, 1);
        String value = (String) getValueAt(modelRow, 2);
        new AttributeDeleteDialog(parentFrame, id, attributeName + ": " + value, this);
    }
}
