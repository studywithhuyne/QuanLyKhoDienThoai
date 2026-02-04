package ui.account;

import javax.swing.*;

import bus.AccountBUS;
import dto.AccountDTO;
import ui.BaseCrudPanel;

import java.util.List;
import java.text.SimpleDateFormat;

/**
 * Panel quản lý Tài khoản
 */
public class AccountPanel extends BaseCrudPanel {
    
    private static final String[] COLUMNS = {"ID", "Username", "Họ tên", "Vai trò", "Ngày tạo"};
    private final AccountBUS accountBUS;
    
    public AccountPanel(JFrame parentFrame) {
        super(parentFrame, "tài khoản", COLUMNS);
        this.accountBUS = new AccountBUS();
    }
    
    @Override
    public void loadData() {
        List<AccountDTO> accounts = accountBUS.getAll();
        
        tableModel.setRowCount(0);
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        
        for (AccountDTO account : accounts) {
            String createdAt = account.getCreatedAt() != null ? 
                formatter.format(account.getCreatedAt()) : "";
            String role = account.getRole().equals("admin") ? "Admin" : "Staff";
            
            tableModel.addRow(new Object[]{
                account.getID(),
                account.getUsername(),
                account.getFullname(),
                role,
                createdAt
            });
        }
    }

    @Override
    protected void setupColumnWidths() {
        try {
            setFixedColumnWidth(0, 60);   // ID
            setFixedColumnWidth(3, 90);   // Vai trò
            setFixedColumnWidth(4, 120);  // Ngày tạo
        } catch (Exception ex) {
            // Ignore if table not ready
        }
    }

    @Override
    protected void onAddAction() {
        new AccountAddDialog(parentFrame);
        loadData();
    }

    @Override
    protected void onEditAction(int modelRow) {
        int id = (int) tableModel.getValueAt(modelRow, 0);
        String username = String.valueOf(tableModel.getValueAt(modelRow, 1));
        String fullName = String.valueOf(tableModel.getValueAt(modelRow, 2));
        String role = String.valueOf(tableModel.getValueAt(modelRow, 3));
        new AccountEditDialog(parentFrame, id, username, fullName, role, this);
    }

    @Override
    protected void onDeleteAction(int modelRow) {
        int id = (int) tableModel.getValueAt(modelRow, 0);
        String username = String.valueOf(tableModel.getValueAt(modelRow, 1));
        new AccountDeleteDialog(parentFrame, id, username, this);
    }
}
