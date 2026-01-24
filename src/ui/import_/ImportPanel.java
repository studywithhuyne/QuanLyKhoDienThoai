package ui.import_;

import javax.swing.*;

import ui.BasePanel;

/**
 * Panel quản lý Phiếu nhập hàng
 */
public class ImportPanel extends BasePanel {
    
    private static final String[] COLUMNS = {"ID", "Nhà cung cấp", "Nhân viên", "Tổng tiền", "Ngày tạo"};
    private static final Object[][] DATA = {
        {1, "FPT Synnex", "Jerry", "1,500,000,000₫", "02/01/2026"},
    };
    
    public ImportPanel(JFrame parentFrame) {
        super(parentFrame, "Phiếu nhập", COLUMNS, DATA);
    }
}
