package ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;

import ui.product.ProductAddUI;
import ui.product.ProductDeleteUI;
import ui.product.ProductEditUI;

import static ui.UIColor.*;

/**
 * Panel quản lý Sản phẩm
 */
public class ProductPanel extends JPanel {
    
    private JFrame parentFrame;
    private JTable productTable;
    
    private Object[][] productData = {
        {1, "iPhone 17 Pro Max", "Apple", "Điện thoại", 2, 13},
        {2, "Samsung Galaxy S26 Ultra", "Samsung", "Điện thoại", 2, 15},
        {3, "Xiaomi 15 Ultra", "Xiaomi", "Điện thoại", 0, 0},
        {4, "Anker PowerLine III Flow USB-C", "Anker", "Cáp sạc", 1, 50},
        {5, "Baseus Crystal Shine Cable", "Baseus", "Cáp sạc", 1, 60},
    };
    private String[] productColumns = {"ID", "Tên sản phẩm", "Thương hiệu", "Danh mục", "SKU", "Tồn kho"};
    
    public ProductPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(CONTENT_BG);
        setBorder(new EmptyBorder(25, 30, 25, 30));
        
        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionPanel.setBackground(CONTENT_BG);
        actionPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JButton addBtn = createActionButton("➕ Thêm mới", DARK_BLUE);
        JButton editBtn = createActionButton("✏️ Sửa", WARNING_COLOR);
        JButton deleteBtn = createActionButton("🗑️ Xóa", DANGER_COLOR);
        JButton refreshBtn = createActionButton("🔄 Làm mới", GREEN);
        
        // Add button action
        addBtn.addActionListener(e -> new ProductAddUI(parentFrame));
        
        // Edit button action
        editBtn.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, "Vui lòng chọn sản phẩm cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) productTable.getValueAt(selectedRow, 0);
            String name = (String) productTable.getValueAt(selectedRow, 1);
            String brand = (String) productTable.getValueAt(selectedRow, 2);
            String category = (String) productTable.getValueAt(selectedRow, 3);
            new ProductEditUI(parentFrame, id, name, brand, category);
        });
        
        // Delete button action
        deleteBtn.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, "Vui lòng chọn sản phẩm cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) productTable.getValueAt(selectedRow, 0);
            String name = (String) productTable.getValueAt(selectedRow, 1);
            new ProductDeleteUI(parentFrame, id, name);
        });
        
        // Refresh button action
        refreshBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(parentFrame, "Đã làm mới dữ liệu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });
        
        actionPanel.add(addBtn);
        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);
        actionPanel.add(refreshBtn);
        
        add(actionPanel, BorderLayout.NORTH);
        
        // Table
        productTable = new JTable(productData, productColumns);
        setupTable();
        setupColumnWidths();
        setupContextMenu();
        
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scrollPane.getViewport().setBackground(CARD_BG);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupTable() {
        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        productTable.setRowHeight(45);
        productTable.setShowGrid(false);
        productTable.setIntercellSpacing(new Dimension(0, 0));
        productTable.setBackground(CARD_BG);
        productTable.setSelectionBackground(new Color(79, 70, 229, 30));
        productTable.setSelectionForeground(TEXT_PRIMARY);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Header style
        productTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        productTable.getTableHeader().setBackground(CARD_BG);
        productTable.getTableHeader().setForeground(TEXT_PRIMARY);
        productTable.getTableHeader().setPreferredSize(new Dimension(0, 50));
        productTable.getTableHeader().setBorder(new MatteBorder(0, 0, 2, 0, BORDER_COLOR));
        
        // Cell renderer for alternating row colors
        productTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? CARD_BG : new Color(249, 250, 251));
                }
                setBorder(new EmptyBorder(0, 15, 0, 15));
                return c;
            }
        });
    }
    
    private void setupColumnWidths() {
        TableColumnModel columnModel = productTable.getColumnModel();
        
        // Cột ID (Cố định 50px)
        columnModel.getColumn(0).setMinWidth(50);
        columnModel.getColumn(0).setMaxWidth(50);
        columnModel.getColumn(0).setPreferredWidth(50);
        
        // Cột Tên sản phẩm (Co giãn tự do)
        columnModel.getColumn(1).setMinWidth(200);
        columnModel.getColumn(1).setPreferredWidth(400);
        
        // Cột Thương hiệu
        columnModel.getColumn(2).setMinWidth(100);
        columnModel.getColumn(2).setMaxWidth(150);
        columnModel.getColumn(2).setPreferredWidth(120);
        
        // Cột Danh mục
        columnModel.getColumn(3).setMinWidth(100);
        columnModel.getColumn(3).setMaxWidth(150);
        columnModel.getColumn(3).setPreferredWidth(120);
        
        // Cột SKU
        columnModel.getColumn(4).setMinWidth(100);
        columnModel.getColumn(4).setMaxWidth(100);
        columnModel.getColumn(4).setPreferredWidth(100);
        
        // Cột Tồn kho
        columnModel.getColumn(5).setMinWidth(80);
        columnModel.getColumn(5).setMaxWidth(80);
        columnModel.getColumn(5).setPreferredWidth(80);
    }
    
    private void setupContextMenu() {
        JPopupMenu contextMenu = new JPopupMenu();
        contextMenu.setBackground(CARD_BG);
        contextMenu.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        
        JMenuItem menuAdd = createMenuItem("➕ Thêm mới", DARK_BLUE);
        JMenuItem menuEdit = createMenuItem("✏️ Sửa", WARNING_COLOR);
        JMenuItem menuDelete = createMenuItem("🗑️ Xóa", DANGER_COLOR);
        
        menuAdd.addActionListener(e -> new ProductAddUI(parentFrame));
        
        menuEdit.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, "Vui lòng chọn sản phẩm cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) productTable.getValueAt(selectedRow, 0);
            String name = (String) productTable.getValueAt(selectedRow, 1);
            String brand = (String) productTable.getValueAt(selectedRow, 2);
            String category = (String) productTable.getValueAt(selectedRow, 3);
            new ProductEditUI(parentFrame, id, name, brand, category);
        });
        
        menuDelete.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, "Vui lòng chọn sản phẩm cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) productTable.getValueAt(selectedRow, 0);
            String name = (String) productTable.getValueAt(selectedRow, 1);
            new ProductDeleteUI(parentFrame, id, name);
        });
        
        contextMenu.add(menuAdd);
        contextMenu.addSeparator();
        contextMenu.add(menuEdit);
        contextMenu.add(menuDelete);
        
        // Add mouse listener for right-click
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) showContextMenu(e);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) showContextMenu(e);
            }
            
            private void showContextMenu(MouseEvent e) {
                int row = productTable.rowAtPoint(e.getPoint());
                if (row >= 0 && row < productTable.getRowCount()) {
                    productTable.setRowSelectionInterval(row, row);
                }
                contextMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }
    
    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else {
                    g2.setColor(getBackground());
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        button.setForeground(color);
        button.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        button.setPreferredSize(new Dimension(120, 38));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color);
                button.setForeground(Color.WHITE);
                button.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                button.setForeground(color);
                button.repaint();
            }
        });
        
        return button;
    }
    
    private JMenuItem createMenuItem(String text, Color color) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        menuItem.setForeground(TEXT_PRIMARY);
        menuItem.setBackground(CARD_BG);
        menuItem.setBorder(new EmptyBorder(8, 15, 8, 15));
        menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menuItem.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                menuItem.setForeground(color);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                menuItem.setBackground(CARD_BG);
                menuItem.setForeground(TEXT_PRIMARY);
            }
        });
        
        return menuItem;
    }
}
