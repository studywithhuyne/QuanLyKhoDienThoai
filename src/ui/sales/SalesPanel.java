package ui.sales;

import javax.swing.*;
import java.util.List;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import dao.InvoiceDAO;
import dto.InvoiceDTO;
import ui.BaseCrudPanel;

/**
 * Panel quản lý Phiếu xuất hàng / Hóa đơn
 */
public class SalesPanel extends BaseCrudPanel {
    
    private static final String[] COLUMNS = {"ID", "Nhân viên", "Tổng tiền", "Ngày tạo"};
    
    public SalesPanel(JFrame parentFrame) {
        super(parentFrame, "hóa đơn", COLUMNS);
    }
    
    @Override
    public void loadData() {
        InvoiceDAO invoiceDAO = new InvoiceDAO();
        List<InvoiceDTO> invoices = invoiceDAO.GetAllInvoice();
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
        new SalesAddDialog(parentFrame, this);
    }
    
    @Override
    protected void onEditAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String staffName = (String) getValueAt(modelRow, 1);
        new SalesEditDialog(parentFrame, id, staffName, this);
    }
    
    @Override
    protected void onDeleteAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String staffName = (String) getValueAt(modelRow, 1);
        new SalesDeleteDialog(parentFrame, id, "Hóa đơn #" + id + " - " + staffName, this);
    }
}
