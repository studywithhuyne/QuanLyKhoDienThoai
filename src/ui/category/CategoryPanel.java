package ui.category;

import javax.swing.*;

import ui.DataPanel;

/**
 * Panel quản lý Danh mục sản phẩm
 */
public class CategoryPanel extends DataPanel {
    
    private static final String[] COLUMNS = {"ID", "Tên danh mục"};
    private static final Object[][] DATA = {
        {1, "Điện thoại"},
        {2, "Cáp sạc"},
        {3, "Cường lực"},
        {4, "Sạc dự phòng"},
        {5, "Củ sạc"},
        {6, "Loa"},
    };
    
    public CategoryPanel(JFrame parentFrame) {
        super(parentFrame, "Danh mục", COLUMNS, DATA);
    }
}
