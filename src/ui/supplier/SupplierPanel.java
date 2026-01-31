package ui.supplier;

import javax.swing.*;
import java.util.List;

import bus.SupplierBUS;
import dto.SupplierDTO;
import ui.BaseCrudPanel;

/**
 * Panel quản lý Nhà cung cấp
 */
public class SupplierPanel extends BaseCrudPanel {
    
    private static final String[] COLUMNS = {"ID", "Tên nhà cung cấp", "Số điện thoại", "Email", "Địa chỉ"};
    private SupplierBUS supplierBUS = new SupplierBUS();
    
    public SupplierPanel(JFrame parentFrame) {
        super(parentFrame, "nhà cung cấp", COLUMNS);
    }
    
    @Override
    public void loadData() {
        List<SupplierDTO> suppliers = supplierBUS.getAll();
        tableModel.setRowCount(0);
        for (SupplierDTO supplier : suppliers) {
            tableModel.addRow(new Object[]{
                supplier.getID(), 
                supplier.getName(),
                supplier.getPhone(),
                supplier.getEmail(),
                supplier.getAddress()
            });
        }
    }
    
    @Override
    protected void setupColumnWidths() {
        setFixedColumnWidth(0, 60);   // ID
        setFlexibleColumnWidth(1, 150, 200); // Tên nhà cung cấp
        setFixedColumnWidth(2, 120);  // SĐT
        setFlexibleColumnWidth(3, 180, 220); // Email
        setFlexibleColumnWidth(4, 200, 300); // Địa chỉ
    }
    
    @Override
    protected void onAddAction() {
        new SupplierAddDialog(parentFrame, this);
    }
    
    @Override
    protected void onEditAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String name = (String) getValueAt(modelRow, 1);
        String phone = (String) getValueAt(modelRow, 2);
        String email = (String) getValueAt(modelRow, 3);
        String address = (String) getValueAt(modelRow, 4);
        new SupplierEditDialog(parentFrame, id, name, phone, email, address, this);
    }
    
    @Override
    protected void onDeleteAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String name = (String) getValueAt(modelRow, 1);
        new SupplierDeleteDialog(parentFrame, id, name, this);
    }
}
