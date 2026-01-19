package ui.attribute;

import javax.swing.*;

import ui.BasePanel;

/**
 * Panel quản lý Thuộc tính sản phẩm
 */
public class AttributePanel extends BasePanel {
    
    private static final String[] COLUMNS = {"ID", "Tên thuộc tính", "Giá trị"};
    private static final Object[][] DATA = {
        {1, "Màu sắc", "Đen"},
        {2, "Dung lượng", "64GB"},
        {3, "RAM", "4GB"},
        {4, "Chiều dài cáp", "0.5m"},
        {5, "Công suất sạc", "20W"},
    };
    
    public AttributePanel(JFrame parentFrame) {
        super(parentFrame, "Thuộc tính", COLUMNS, DATA);
    }
}
