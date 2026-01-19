package ui.account;

import javax.swing.*;

import ui.BasePanel;

/**
 * Panel quản lý Tài khoản
 */
public class AccountPanel extends BasePanel {
    
    private static final String[] COLUMNS = {"ID", "Username", "Họ tên", "Vai trò", "Ngày tạo"};
    private static final Object[][] DATA = {
        {1, "admin", "Quản lý", "Admin", "01/01/2026"},
        {2, "jerry", "Jerry", "Staff", "01/01/2026"},
    };
    
    public AccountPanel(JFrame parentFrame) {
        super(parentFrame, "Tài khoản", COLUMNS, DATA);
    }
}
