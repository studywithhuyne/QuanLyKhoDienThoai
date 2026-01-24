package ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

import static utils.ColorUtil.*;

import java.awt.*;
import java.awt.event.*;

/**
 * Base Panel cho các màn hình quản lý CRUD với bảng dữ liệu
 * Các lớp con cần override các phương thức trừu tượng để tùy chỉnh hành vi
 */
public abstract class BaseCrudPanel extends JPanel implements ISearchable {
    
    protected JFrame parentFrame;
    protected JTable dataTable;
    protected DefaultTableModel tableModel;
    protected TableRowSorter<DefaultTableModel> rowSorter;
    
    protected String entityName; // Tên đối tượng (VD: "thương hiệu", "danh mục", "sản phẩm")
    protected String[] columns;
    
    public BaseCrudPanel(JFrame parentFrame, String entityName, String[] columns) {
        this.parentFrame = parentFrame;
        this.entityName = entityName;
        this.columns = columns;
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
        addBtn.addActionListener(e -> onAddAction());
        
        // Edit button action
        editBtn.addActionListener(e -> {
            int selectedRow = dataTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Vui lòng chọn " + entityName + " cần sửa!", 
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int modelRow = dataTable.convertRowIndexToModel(selectedRow);
            onEditAction(modelRow);
        });
        
        // Delete button action
        deleteBtn.addActionListener(e -> {
            int selectedRow = dataTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Vui lòng chọn " + entityName + " cần xóa!", 
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int modelRow = dataTable.convertRowIndexToModel(selectedRow);
            onDeleteAction(modelRow);
        });
        
        // Refresh button action
        refreshBtn.addActionListener(e -> {
            loadData();
            JOptionPane.showMessageDialog(parentFrame, 
                "Đã làm mới dữ liệu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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
        searchField.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true), 
            new EmptyBorder(5, 15, 5, 15)
        ));
        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm " + entityName + "...");
        
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
        tableModel = new DefaultTableModel(new Object[0][0], columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dataTable = new JTable(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        dataTable.setRowSorter(rowSorter);
        
        loadData();
        setupTable();
        setupColumnWidths();
        setupContextMenu();
        
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scrollPane.getViewport().setBackground(CARD_BG);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupTable() {
        dataTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dataTable.setRowHeight(45);
        dataTable.setShowGrid(false);
        dataTable.setIntercellSpacing(new Dimension(0, 0));
        dataTable.setBackground(CARD_BG);
        dataTable.setSelectionBackground(SELECTION_BG);
        dataTable.setSelectionForeground(TEXT_PRIMARY);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Header style
        dataTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        dataTable.getTableHeader().setBackground(CARD_BG);
        dataTable.getTableHeader().setForeground(TEXT_PRIMARY);
        dataTable.getTableHeader().setPreferredSize(new Dimension(0, 50));
        dataTable.getTableHeader().setBorder(new MatteBorder(0, 0, 2, 0, BORDER_COLOR));
        
        // Cell renderer for alternating row colors
        dataTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? CARD_BG : TABLE_ROW_ALT);
                }
                setBorder(new EmptyBorder(0, 15, 0, 15));
                return c;
            }
        });
    }
    
    private void setupContextMenu() {
        JPopupMenu contextMenu = new JPopupMenu();
        contextMenu.setBackground(CARD_BG);
        contextMenu.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        
        JMenuItem menuAdd = createMenuItem("➕ Thêm mới", DARK_BLUE);
        JMenuItem menuEdit = createMenuItem("✏️ Sửa", WARNING_COLOR);
        JMenuItem menuDelete = createMenuItem("🗑️ Xóa", DANGER_COLOR);
        
        menuAdd.addActionListener(e -> onAddAction());
        
        menuEdit.addActionListener(e -> {
            int selectedRow = dataTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Vui lòng chọn " + entityName + " cần sửa!", 
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int modelRow = dataTable.convertRowIndexToModel(selectedRow);
            onEditAction(modelRow);
        });
        
        menuDelete.addActionListener(e -> {
            int selectedRow = dataTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Vui lòng chọn " + entityName + " cần xóa!", 
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int modelRow = dataTable.convertRowIndexToModel(selectedRow);
            onDeleteAction(modelRow);
        });
        
        contextMenu.add(menuAdd);
        contextMenu.addSeparator();
        contextMenu.add(menuEdit);
        contextMenu.add(menuDelete);
        
        // Add mouse listener for right-click
        dataTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) showContextMenu(e);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) showContextMenu(e);
            }
            
            private void showContextMenu(MouseEvent e) {
                int row = dataTable.rowAtPoint(e.getPoint());
                if (row >= 0 && row < dataTable.getRowCount()) {
                    dataTable.setRowSelectionInterval(row, row);
                }
                contextMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }
    
    protected JButton createActionButton(String text, Color color) {
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
    
    protected JMenuItem createMenuItem(String text, Color color) {
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
    
    // ==================== Helper methods cho column width ====================
    
    protected void setFixedColumnWidth(int index, int width) {
        TableColumnModel columnModel = dataTable.getColumnModel();
        columnModel.getColumn(index).setMinWidth(width);
        columnModel.getColumn(index).setMaxWidth(width);
        columnModel.getColumn(index).setPreferredWidth(width);
    }
    
    protected void setFlexibleColumnWidth(int index, int minWidth, int preferredWidth) {
        TableColumnModel columnModel = dataTable.getColumnModel();
        columnModel.getColumn(index).setMinWidth(minWidth);
        columnModel.getColumn(index).setPreferredWidth(preferredWidth);
    }
    
    protected void setRangeColumnWidth(int index, int minWidth, int maxWidth, int preferredWidth) {
        TableColumnModel columnModel = dataTable.getColumnModel();
        columnModel.getColumn(index).setMinWidth(minWidth);
        columnModel.getColumn(index).setMaxWidth(maxWidth);
        columnModel.getColumn(index).setPreferredWidth(preferredWidth);
    }
    
    // ==================== Getter cho tableModel ====================
    
    protected Object getValueAt(int row, int column) {
        return tableModel.getValueAt(row, column);
    }
    
    protected DefaultTableModel getTableModel() {
        return tableModel;
    }
    
    protected JFrame getParentFrame() {
        return parentFrame;
    }
    
    // ==================== Abstract methods - lớp con phải implement ====================
    
    /**
     * Load dữ liệu từ database vào bảng
     */
    public abstract void loadData();
    
    /**
     * Thiết lập độ rộng các cột
     */
    protected abstract void setupColumnWidths();
    
    /**
     * Xử lý khi nhấn nút Thêm
     */
    protected abstract void onAddAction();
    
    /**
     * Xử lý khi nhấn nút Sửa
     * @param modelRow Chỉ số dòng trong model (đã convert từ view)
     */
    protected abstract void onEditAction(int modelRow);
    
    /**
     * Xử lý khi nhấn nút Xóa
     * @param modelRow Chỉ số dòng trong model (đã convert từ view)
     */
    protected abstract void onDeleteAction(int modelRow);
    
    // ==================== Search implementation ====================
    
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
