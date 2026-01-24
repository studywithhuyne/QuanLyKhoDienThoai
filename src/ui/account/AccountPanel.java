package ui.account;

import javax.swing.*;

import dao.AccountDAO;
import dto.AccountDTO;
import ui.BasePanel;

import java.util.List;
import java.text.SimpleDateFormat;

/**
 * Panel quản lý Tài khoản
 */
public class AccountPanel extends BasePanel {
    
    private static final String[] COLUMNS = {"ID", "Username", "Họ tên", "Vai trò", "Ngày tạo"};
    
    public AccountPanel(JFrame parentFrame) {
        super(parentFrame, "Tài khoản", COLUMNS, new Object[][]{});
        loadData();
    }
    
    public void loadData() {
        AccountDAO accountDAO = new AccountDAO();
        List<AccountDTO> accounts = accountDAO.GetAllAccount();
        
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
