package ui.sales;

import javax.swing.*;

import ui.BasePanel;

/**
 * Panel quản lý Phiếu xuất hàng / Hóa đơn
 */
public class SalesPanel extends BasePanel {
    
    private static final String[] COLUMNS = {"ID", "Nhân viên", "Tổng tiền", "Ngày tạo"};
    private static final Object[][] DATA = {
        {1, "Jerry", "56,490,000₫", "05/01/2026"},
    };
    
    public SalesPanel(JFrame parentFrame) {
        super(parentFrame, "Hóa đơn", COLUMNS, DATA);
    }
}
