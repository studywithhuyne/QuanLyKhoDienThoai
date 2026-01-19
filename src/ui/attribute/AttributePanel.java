package ui.attribute;

import javax.swing.*;

import ui.DataPanel;

/**
 * Panel quản lý Thuộc tính sản phẩm
 */
public class AttributePanel extends DataPanel {
    
    private static final String[] COLUMNS = {"ID", "Tên thuộc tính", "Giá trị"};
    private static final Object[][] DATA = {
        {1, "Màu sắc", "Đen, Trắng, Xanh, Hồng"},
        {2, "Dung lượng", "64GB, 128GB, 256GB, 512GB, 1TB"},
        {3, "RAM", "4GB, 6GB, 8GB, 12GB, 16GB"},
        {4, "Chiều dài cáp", "0.5m, 1m, 1.5m, 2m, 3m"},
        {5, "Công suất sạc", "20W, 30W, 45W, 65W, 100W"},
    };
    
    public AttributePanel(JFrame parentFrame) {
        super(parentFrame, "Thuộc tính", COLUMNS, DATA);
    }
}
