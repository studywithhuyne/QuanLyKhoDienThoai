package ui.sku;

import javax.swing.*;

import ui.DataPanel;

/**
 * Panel quản lý SKU (Stock Keeping Unit)
 */
public class SkuPanel extends DataPanel {
    
    private static final String[] COLUMNS = {"ID", "Mã SKU", "Sản phẩm", "Thuộc tính", "Giá nhập", "Giá bán", "Tồn kho"};
    private static final Object[][] DATA = {
        {1, "IP17PM-BLK-256", "iPhone 17 Pro Max", "Đen, 256GB", "28,000,000₫", "32,990,000₫", 5},
        {2, "IP17PM-WHT-256", "iPhone 17 Pro Max", "Trắng, 256GB", "28,000,000₫", "32,990,000₫", 3},
        {3, "IP17PM-BLK-512", "iPhone 17 Pro Max", "Đen, 512GB", "32,000,000₫", "36,990,000₫", 2},
        {4, "SS-S26U-BLK-256", "Samsung Galaxy S26 Ultra", "Đen, 256GB", "25,000,000₫", "29,990,000₫", 8},
        {5, "SS-S26U-WHT-512", "Samsung Galaxy S26 Ultra", "Trắng, 512GB", "29,000,000₫", "33,990,000₫", 4},
        {6, "ANK-PL3-1M", "Anker PowerLine III Flow USB-C", "1m", "200,000₫", "350,000₫", 50},
    };
    
    public SkuPanel(JFrame parentFrame) {
        super(parentFrame, "SKU", COLUMNS, DATA);
    }
}
