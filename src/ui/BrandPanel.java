package ui;

import javax.swing.*;

/**
 * Panel quản lý Thương hiệu
 */
public class BrandPanel extends DataPanel {
    
    private static final String[] COLUMNS = {"ID", "Tên thương hiệu"};
    private static final Object[][] DATA = {
        {1, "Apple"},
        {2, "Samsung"},
        {3, "Xiaomi"},
        {4, "Oppo"},
        {5, "Anker"},
        {6, "Baseus"},
        {7, "Belkin"},
        {8, "Sony"},
        {9, "Ugreen"},
    };
    
    public BrandPanel(JFrame parentFrame) {
        super(parentFrame, "Thương hiệu", COLUMNS, DATA);
    }
}
