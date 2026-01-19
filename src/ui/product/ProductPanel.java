package ui.product;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import dao.ProductDAO;
import ui.Searchable;
import static ui.UIColor.*;

/**
 * Panel quản lý Sản phẩm
 */
public class ProductPanel extends JPanel implements Searchable {
    
    private JFrame parentFrame;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    
    private Object[][] productData = new Object[0][0];
    private String[] productColumns = {"ID", "Tên sản phẩm", "Thương hiệu", "Danh mục", "Tồn kho"};
    
    public ProductPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(CONTENT_BG);
        setBorder(new EmptyBorder(25, 30, 25, 30));
        
        // Action buttons panel với search field
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(CONTENT_BG);
        actionPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Left side - buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonsPanel.setBackground(CONTENT_BG);
        
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
            int modelRow = productTable.convertRowIndexToModel(selectedRow);
            int id = (int) tableModel.getValueAt(modelRow, 0);
            String name = (String) tableModel.getValueAt(modelRow, 1);
            String brand = (String) tableModel.getValueAt(modelRow, 2);
            String category = (String) tableModel.getValueAt(modelRow, 3);
            new ProductEditUI(parentFrame, id, name, brand, category);
        });
        
        // Delete button action
        deleteBtn.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, "Vui lòng chọn sản phẩm cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int modelRow = productTable.convertRowIndexToModel(selectedRow);
            int id = (int) tableModel.getValueAt(modelRow, 0);
            String name = (String) tableModel.getValueAt(modelRow, 1);
            new ProductDeleteUI(parentFrame, id, name);
        });
        
        // Refresh button action
        refreshBtn.addActionListener(e -> {
            loadProductData();
            JOptionPane.showMessageDialog(parentFrame, "Đã làm mới dữ liệu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });
        
        buttonsPanel.add(addBtn);
        buttonsPanel.add(editBtn);
        buttonsPanel.add(deleteBtn);
        buttonsPanel.add(refreshBtn);
        
        actionPanel.add(buttonsPanel, BorderLayout.WEST);
        
        // Right side - search field
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        searchPanel.setBackground(CONTENT_BG);
        
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(250, 38));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR, 1, true), new EmptyBorder(5, 15, 5, 15)));
        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm sản phẩm...");
        
        // DocumentListener để tìm kiếm realtime
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(searchField.getText());
            }
        });
        
        searchPanel.add(searchField);
        actionPanel.add(searchPanel, BorderLayout.EAST);
        
        add(actionPanel, BorderLayout.NORTH);
        
        // Table with DefaultTableModel for filtering
        tableModel = new DefaultTableModel(productData, productColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        productTable.setRowSorter(rowSorter);
        loadProductData();
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
        productTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        setFixedColumnWidth(columnModel, 0, 50);  // ID
        setFlexibleColumnWidth(columnModel, 1, 200, 400); // Tên sản phẩm
        setFixedRangeColumnWidth(columnModel, 2, 100, 150, 120); // Thương hiệu
        setFixedRangeColumnWidth(columnModel, 3, 100, 150, 120); // Danh mục
        setFixedColumnWidth(columnModel, 4, 80); // Tồn kho
    }

    private void setFixedColumnWidth(TableColumnModel columnModel, int index, int width) {
        columnModel.getColumn(index).setMinWidth(width);
        columnModel.getColumn(index).setMaxWidth(width);
        columnModel.getColumn(index).setPreferredWidth(width);
    }

    private void setFlexibleColumnWidth(TableColumnModel columnModel, int index, int minWidth, int preferredWidth) {
        columnModel.getColumn(index).setMinWidth(minWidth);
        columnModel.getColumn(index).setPreferredWidth(preferredWidth);
    }

    private void setFixedRangeColumnWidth(TableColumnModel columnModel, int index, int minWidth, int maxWidth, int preferredWidth) {
        columnModel.getColumn(index).setMinWidth(minWidth);
        columnModel.getColumn(index).setMaxWidth(maxWidth);
        columnModel.getColumn(index).setPreferredWidth(preferredWidth);
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
            int modelRow = productTable.convertRowIndexToModel(selectedRow);
            int id = (int) tableModel.getValueAt(modelRow, 0);
            String name = (String) tableModel.getValueAt(modelRow, 1);
            String brand = (String) tableModel.getValueAt(modelRow, 2);
            String category = (String) tableModel.getValueAt(modelRow, 3);
            new ProductEditUI(parentFrame, id, name, brand, category);
        });
        
        menuDelete.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, "Vui lòng chọn sản phẩm cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int modelRow = productTable.convertRowIndexToModel(selectedRow);
            int id = (int) tableModel.getValueAt(modelRow, 0);
            String name = (String) tableModel.getValueAt(modelRow, 1);
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

    private void loadProductData() {
        ProductDAO productDAO = new ProductDAO();
        List<Object[]> rows = productDAO.GetProductTableData();
        tableModel.setRowCount(0);
        for (Object[] row : rows) {
            tableModel.addRow(row);
        }
    }
 
    @Override
    public void search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            // Tìm kiếm không phân biệt hoa thường trên tất cả các cột
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword.trim()));
        }
    }
}
