package ui.logs;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;

import ui.Searchable;
import static ui.UIColor.*;

/**
 * Panel quản lý Logs - Chỉ xem, không có chức năng thêm/sửa/xóa
 */
public class LogsPanel extends JPanel implements Searchable {
    
    private JFrame parentFrame;
    private JTable logsTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    
    private static final String[] COLUMNS = {"ID", "Thời gian", "Người dùng", "Hành động", "Chi tiết"};
    private static final Object[][] DATA = {
        {1, "18/01/2026 10:30:00", "admin", "Đăng nhập", "Đăng nhập thành công"},
        {2, "18/01/2026 10:32:15", "admin", "Thêm sản phẩm", "Thêm sản phẩm: iPhone 17 Pro Max"},
        {3, "18/01/2026 10:45:30", "jerry", "Đăng nhập", "Đăng nhập thành công"},
        {4, "18/01/2026 11:00:00", "jerry", "Tạo phiếu nhập", "Phiếu nhập #1 - FPT Synnex"},
        {5, "18/01/2026 11:30:45", "jerry", "Bán hàng", "Hóa đơn #1 - iPhone 17 Pro Max"},
        {6, "18/01/2026 12:00:00", "admin", "Sửa sản phẩm", "Cập nhật giá: Samsung Galaxy S26 Ultra"},
        {7, "18/01/2026 14:15:20", "jerry", "Xóa IMEI", "Xóa IMEI: 352789100456792"},
        {8, "18/01/2026 15:30:00", "admin", "Đăng xuất", "Đăng xuất thành công"},
    };
    
    public LogsPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(CONTENT_BG);
        setBorder(new EmptyBorder(25, 30, 25, 30));
        
        // Action panel - chỉ có nút làm mới và search
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(CONTENT_BG);
        actionPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Left side - refresh button only
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonsPanel.setBackground(CONTENT_BG);
        
        JButton refreshBtn = createActionButton("🔄 Làm mới", GREEN);
        refreshBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(parentFrame, "Đã làm mới dữ liệu logs!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });
        
        buttonsPanel.add(refreshBtn);
        actionPanel.add(buttonsPanel, BorderLayout.WEST);
        
        // Right side - search field
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        searchPanel.setBackground(CONTENT_BG);
        
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(250, 38));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR, 1, true), new EmptyBorder(5, 15, 5, 15)));
        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm logs...");
        
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { search(searchField.getText()); }
            @Override
            public void removeUpdate(DocumentEvent e) { search(searchField.getText()); }
            @Override
            public void changedUpdate(DocumentEvent e) { search(searchField.getText()); }
        });
        
        searchPanel.add(searchField);
        actionPanel.add(searchPanel, BorderLayout.EAST);
        
        add(actionPanel, BorderLayout.NORTH);
        
        // Table
        tableModel = new DefaultTableModel(DATA, COLUMNS) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        logsTable = new JTable(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        logsTable.setRowSorter(rowSorter);
        setupTable();
        
        JScrollPane scrollPane = new JScrollPane(logsTable);
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scrollPane.getViewport().setBackground(CARD_BG);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupTable() {
        logsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logsTable.setRowHeight(45);
        logsTable.setShowGrid(false);
        logsTable.setIntercellSpacing(new Dimension(0, 0));
        logsTable.setBackground(CARD_BG);
        logsTable.setSelectionBackground(new Color(79, 70, 229, 30));
        logsTable.setSelectionForeground(TEXT_PRIMARY);
        
        // Header style
        logsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        logsTable.getTableHeader().setBackground(CARD_BG);
        logsTable.getTableHeader().setForeground(TEXT_PRIMARY);
        logsTable.getTableHeader().setPreferredSize(new Dimension(0, 50));
        logsTable.getTableHeader().setBorder(new MatteBorder(0, 0, 2, 0, BORDER_COLOR));
        
        // Cell renderer for alternating row colors
        logsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
        
        return button;
    }
    
    @Override
    public void search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword.trim()));
        }
    }
}
