package ui.logs;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

import static utils.ColorUtil.*;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

import dao.LogDAO;
import dto.LogDTO;
import ui.ISearchable;

/**
 * Panel quản lý Logs - Chỉ xem, không có chức năng thêm/sửa/xóa
 */
public class LogsPanel extends JPanel implements ISearchable {
    
    private JFrame parentFrame;
    private JTable logsTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    
    private static final String[] COLUMNS = {"ID", "Thời gian", "Người dùng", "Hành động", "Chi tiết"};
    
    public LogsPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        initializePanel();
        loadData();
    }
    
    public void loadData() {
        LogDAO logDAO = new LogDAO();
        List<LogDTO> logs = logDAO.GetAllLogs();
        tableModel.setRowCount(0);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        for (LogDTO log : logs) {
            String dateStr = log.getCreatedAt() != null ? sdf.format(log.getCreatedAt()) : "";
            tableModel.addRow(new Object[]{
                log.getID(),
                dateStr,
                log.getUsername(),
                log.getAction(),
                log.getDetails()
            });
        }
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
            loadData();
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
        tableModel = new DefaultTableModel(COLUMNS, 0) {
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
        logsTable.setSelectionBackground(SELECTION_BG);
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
                    c.setBackground(row % 2 == 0 ? CARD_BG : TABLE_ROW_ALT);
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
