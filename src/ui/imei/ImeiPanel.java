package ui.imei;

import javax.swing.*;

import ui.BasePanel;

/**
 * Panel quản lý IMEI (International Mobile Equipment Identity)
 */
public class ImeiPanel extends BasePanel {
    
    private static final String[] COLUMNS = {"ID", "IMEI", "SKU", "Sản phẩm", "Trạng thái", "Ngày nhập"};
    private static final Object[][] DATA = {
        {1, "352789100456789", "IP17PM-BLK-256", "iPhone 17 Pro Max", "Còn hàng", "02/01/2026"},
        {2, "352789100456790", "IP17PM-BLK-256", "iPhone 17 Pro Max", "Đã bán", "02/01/2026"},
        {3, "352789100456791", "IP17PM-WHT-256", "iPhone 17 Pro Max", "Còn hàng", "02/01/2026"},
        {4, "354678912345678", "SS-S26U-BLK-256", "Samsung Galaxy S26 Ultra", "Còn hàng", "03/01/2026"},
        {5, "354678912345679", "SS-S26U-BLK-256", "Samsung Galaxy S26 Ultra", "Đã bán", "03/01/2026"},
    };
    
    public ImeiPanel(JFrame parentFrame) {
        super(parentFrame, "IMEI", COLUMNS, DATA);
    }
}
