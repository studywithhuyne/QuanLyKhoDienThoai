package ui.invoice;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import bus.InvoiceBUS;
import dto.InvoiceDTO;
import ui.BaseCrudPanel;

/**
 * Panel quản lý Phiếu xuất hàng
 */
public class InvoicePanel extends BaseCrudPanel {
    
    private static final String[] COLUMNS = {"ID", "Nhân viên", "Tổng tiền", "Ngày tạo"};
    
    public InvoicePanel(JFrame parentFrame) {
        super(parentFrame, "phiếu xuất", COLUMNS);
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
                        new InvoiceDetailDialog(parentFrame, id);
                    }
                }
            }
        });
    }
    
    @Override
    public void loadData() {
        InvoiceBUS invoiceBUS = new InvoiceBUS();
        List<InvoiceDTO> invoices = invoiceBUS.getAll();
        tableModel.setRowCount(0);
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (InvoiceDTO invoice : invoices) {
            tableModel.addRow(new Object[]{
                invoice.getID(), 
                invoice.getStaffName(),
                currencyFormat.format(invoice.getTotalAmount()) + "₫",
                invoice.getCreatedAt() != null ? dateFormat.format(invoice.getCreatedAt()) : ""
            });
        }
    }
    
    @Override
    protected void setupColumnWidths() {
        setFixedColumnWidth(0, 60);   // ID
        setFlexibleColumnWidth(1, 150, 250); // Nhân viên
        setFlexibleColumnWidth(2, 150, 200); // Tổng tiền
        setFlexibleColumnWidth(3, 150, 200); // Ngày tạo
    }
    
    @Override
    protected void onAddAction() {
        new InvoiceAddDialog(parentFrame, this);
    }
    
    @Override
    protected void onEditAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String staffName = (String) getValueAt(modelRow, 1);
        new InvoiceEditDialog(parentFrame, id, staffName, this);
    }
    
    @Override
    protected void onDeleteAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String staffName = (String) getValueAt(modelRow, 1);
        new InvoiceDeleteDialog(parentFrame, id, "Phiếu xuất #" + id + " - " + staffName, this);
    }
    
    @Override
    protected boolean supportsViewAction() {
        return true;
    }
    
    @Override
    protected void onViewAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        new InvoiceDetailDialog(parentFrame, id);
    }
}
