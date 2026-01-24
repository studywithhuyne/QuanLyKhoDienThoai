package ui.imei;

import javax.swing.*;
import java.util.List;
import java.text.SimpleDateFormat;

import dao.ImeiDAO;
import dto.ImeiDTO;
import ui.BaseCrudPanel;

/**
 * Panel quản lý IMEI (International Mobile Equipment Identity)
 */
public class ImeiPanel extends BaseCrudPanel {
    
    private static final String[] COLUMNS = {"ID", "IMEI", "SKU", "Sản phẩm", "Trạng thái", "Ngày nhập"};
    
    public ImeiPanel(JFrame parentFrame) {
        super(parentFrame, "IMEI", COLUMNS);
    }
    
    @Override
    public void loadData() {
        ImeiDAO imeiDAO = new ImeiDAO();
        List<ImeiDTO> imeis = imeiDAO.GetAllImei();
        tableModel.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (ImeiDTO imei : imeis) {
            tableModel.addRow(new Object[]{
                imei.getID(), 
                imei.getImei(),
                imei.getSkuCode(),
                imei.getProductName(),
                imei.getStatusDisplay(),
                imei.getCreatedAt() != null ? dateFormat.format(imei.getCreatedAt()) : ""
            });
        }
    }
    
    @Override
    protected void setupColumnWidths() {
        setFixedColumnWidth(0, 60);   // ID
        setFlexibleColumnWidth(1, 150, 200); // IMEI
        setFlexibleColumnWidth(2, 120, 180); // SKU
        setFlexibleColumnWidth(3, 150, 250); // Sản phẩm
        setFlexibleColumnWidth(4, 100, 120); // Trạng thái
        setFlexibleColumnWidth(5, 100, 120); // Ngày nhập
    }
    
    @Override
    protected void onAddAction() {
        new ImeiAddDialog(parentFrame, this);
    }
    
    @Override
    protected void onEditAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String imei = (String) getValueAt(modelRow, 1);
        new ImeiEditDialog(parentFrame, id, imei, this);
    }
    
    @Override
    protected void onDeleteAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String imei = (String) getValueAt(modelRow, 1);
        new ImeiDeleteDialog(parentFrame, id, imei, this);
    }
}
