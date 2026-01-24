package ui.supplier;

import javax.swing.*;

import ui.BasePanel;

/**
 * Panel quản lý Nhà cung cấp
 */
public class SupplierPanel extends BasePanel {
    
    private static final String[] COLUMNS = {"ID", "Tên nhà cung cấp"};
    private static final Object[][] DATA = {
        {1, "FPT Synnex"},
        {2, "Viettel Store"},
        {3, "CellphoneS B2B"},
        {4, "Anker Vietnam"},
        {5, "Baseus Official"},
        {6, "Ugreen Vietnam"},
    };
    
    public SupplierPanel(JFrame parentFrame) {
        super(parentFrame, "Nhà cung cấp", COLUMNS, DATA);
    }
}
