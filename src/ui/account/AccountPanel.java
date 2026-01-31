package ui.account;

import javax.swing.*;

import bus.AccountBUS;
import dto.AccountDTO;
import ui.BasePanel;

import java.util.List;
import java.text.SimpleDateFormat;

/**
 * Panel quản lý Tài khoản
 */
public class AccountPanel extends BasePanel {
    
    private static final String[] COLUMNS = {"ID", "Username", "Họ tên", "Vai trò", "Ngày tạo"};
    private AccountBUS accountBUS;
    
    public AccountPanel(JFrame parentFrame) {
        super(parentFrame, "Tài khoản", COLUMNS, new Object[][]{});
        this.accountBUS = new AccountBUS();
        loadData();
    }
    
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
}
