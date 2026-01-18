package ui;

import javax.swing.*;

/**
 * Panel quản lý Bán hàng / Hóa đơn
 */
public class SalesPanel extends DataPanel {
    
    private static final String[] COLUMNS = {"ID", "Nhân viên", "Tổng tiền", "Ngày tạo"};
    private static final Object[][] DATA = {
        {1, "Jerry", "56,490,000₫", "05/01/2026"},
    };
    
    public SalesPanel(JFrame parentFrame) {
        super(parentFrame, "Hóa đơn", COLUMNS, DATA);
    }
}
