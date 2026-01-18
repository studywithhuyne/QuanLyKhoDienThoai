package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import static ui.UIColor.*;

/**
 * Base Panel cho các màn hình quản lý dữ liệu với bảng
 */
public class DataPanel extends JPanel {
    
    protected JFrame parentFrame;
    protected JTable dataTable;
    protected String title;
    protected String[] columns;
    protected Object[][] data;
    
    public DataPanel(JFrame parentFrame, String title, String[] columns, Object[][] data) {
        this.parentFrame = parentFrame;
        this.title = title;
        this.columns = columns;
        this.data = data;
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
        
        actionPanel.add(addBtn);
        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);
        actionPanel.add(refreshBtn);
        
        add(actionPanel, BorderLayout.NORTH);
        
        // Table
        dataTable = new JTable(data, columns);
        setupTable();
        
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
        dataTable.setSelectionBackground(new Color(79, 70, 229, 30));
        dataTable.setSelectionForeground(TEXT_PRIMARY);
        
        // Header style
        dataTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        dataTable.getTableHeader().setBackground(CARD_BG);
        dataTable.getTableHeader().setForeground(TEXT_PRIMARY);
        dataTable.getTableHeader().setPreferredSize(new Dimension(0, 50));
        dataTable.getTableHeader().setBorder(new MatteBorder(0, 0, 2, 0, BORDER_COLOR));
        
        // Cell renderer for alternating row colors
        dataTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
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
}
