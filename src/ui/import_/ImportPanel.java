package ui.import_;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import bus.ImportReceiptBUS;
import dto.ImportReceiptDTO;
import ui.BaseCrudPanel;

/**
 * Panel quản lý Phiếu nhập hàng
 */
public class ImportPanel extends BaseCrudPanel {
    
    private static final String[] COLUMNS = {"ID", "Nhà cung cấp", "Nhân viên", "Tổng tiền", "Ngày tạo"};
    private ImportReceiptBUS importReceiptBUS = new ImportReceiptBUS();
    
    public ImportPanel(JFrame parentFrame) {
        super(parentFrame, "phiếu nhập", COLUMNS);
        setupDoubleClick();
    }
    
    private void setupDoubleClick() {
        getDataTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = getDataTable().getSelectedRow();
                    if (selectedRow != -1) {
                        int modelRow = getDataTable().convertRowIndexToModel(selectedRow);
                        int id = (int) getValueAt(modelRow, 0);
                        new ImportDetailDialog(parentFrame, id);
                    }
                }
            }
        });
    }
    
    @Override
    public void loadData() {
        List<ImportReceiptDTO> receipts = importReceiptBUS.getAll();
        tableModel.setRowCount(0);
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm - dd/MM/yyyy");
        for (ImportReceiptDTO receipt : receipts) {
            tableModel.addRow(new Object[]{
                receipt.getID(), 
                receipt.getSupplierName(),
                receipt.getStaffName(),
                currencyFormat.format(receipt.getTotalAmount()) + "₫",
                receipt.getCreatedAt() != null ? dateFormat.format(receipt.getCreatedAt()) : ""
            });
        }
    }
    
    @Override
    protected void setupColumnWidths() {
        setFixedColumnWidth(0, 60);   // ID
        setFlexibleColumnWidth(1, 150, 250); // Nhà cung cấp
        setFlexibleColumnWidth(2, 120, 180); // Nhân viên
        setFlexibleColumnWidth(3, 120, 180); // Tổng tiền
        setFlexibleColumnWidth(4, 130, 180); // Ngày tạo
    }
    
    @Override
    protected void onAddAction() {
        new ImportAddDialog(parentFrame, this);
    }
    
    @Override
    protected void onEditAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String supplierName = (String) getValueAt(modelRow, 1);
        new ImportEditDialog(parentFrame, id, supplierName, this);
    }
    
    @Override
    protected void onDeleteAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String supplierName = (String) getValueAt(modelRow, 1);
        new ImportDeleteDialog(parentFrame, id, "Phiếu nhập #" + id + " - " + supplierName, this);
    }
    
    @Override
    protected boolean supportsViewAction() {
        return true;
    }
    
    @Override
    protected void onViewAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        new ImportDetailDialog(parentFrame, id);
    }
}
