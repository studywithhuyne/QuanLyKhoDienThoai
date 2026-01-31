package ui.sales;

import static utils.ColorUtil.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import dao.InvoiceDAO;
import dto.InvoiceDTO;
import dto.InvoiceDetailDTO;

public class SalesDetailDialog extends JDialog {
    
    private int invoiceId;
    private JTable detailTable;
    private DefaultTableModel tableModel;
    
    private static final String[] COLUMNS = {"STT", "Sản phẩm", "Mã SKU", "Đơn giá", "SL", "IMEI", "Thành tiền"};
    
    public SalesDetailDialog(Frame parent, int invoiceId) {
        super(parent, "Chi tiết phiếu xuất #" + invoiceId, true);
        this.invoiceId = invoiceId;
        
        initializeDialog();
        createComponents();
        loadData();
        setVisible(true);
    }
    
    private void initializeDialog() {
        setSize(900, 600);
        setLocationRelativeTo(getParent());
        setResizable(true);
        setLayout(new BorderLayout());
        getContentPane().setBackground(DIALOG_BG);
    }
    
    private void createComponents() {
        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CARD_BG);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(20, 25, 20, 25)
        ));
        
        JLabel titleLabel = new JLabel("Chi tiết phiếu xuất #" + invoiceId);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        header.add(titleLabel, BorderLayout.WEST);
        
        return header;
    }
    
    private JPanel createContent() {
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(DIALOG_BG);
        contentWrapper.setBorder(new EmptyBorder(20, 25, 10, 25));
        
        // Info panel
        JPanel infoPanel = createInfoPanel();
        contentWrapper.add(infoPanel, BorderLayout.NORTH);
        
        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(DIALOG_BG);
        tablePanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        tableModel = new DefaultTableModel(new Object[0][0], COLUMNS) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        detailTable = new JTable(tableModel);
        setupTable();
        
        JScrollPane scrollPane = new JScrollPane(detailTable);
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scrollPane.getViewport().setBackground(CARD_BG);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        contentWrapper.add(tablePanel, BorderLayout.CENTER);
        
        return contentWrapper;
    }
    
    private JPanel createInfoPanel() {
        JPanel infoCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
            }
        };
        infoCard.setLayout(new GridLayout(1, 4, 20, 0));
        infoCard.setOpaque(false);
        infoCard.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        return infoCard;
    }
    
    private void setupTable() {
        detailTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailTable.setRowHeight(40);
        detailTable.setShowGrid(false);
        detailTable.setIntercellSpacing(new Dimension(0, 0));
        detailTable.setBackground(CARD_BG);
        detailTable.setSelectionBackground(SELECTION_BG);
        detailTable.setSelectionForeground(TEXT_PRIMARY);
        
        // Header style
        detailTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        detailTable.getTableHeader().setBackground(CARD_BG);
        detailTable.getTableHeader().setForeground(TEXT_PRIMARY);
        detailTable.getTableHeader().setPreferredSize(new Dimension(0, 45));
        detailTable.getTableHeader().setBorder(new MatteBorder(0, 0, 2, 0, BORDER_COLOR));
        
        // Column widths
        detailTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // STT
        detailTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Sản phẩm
        detailTable.getColumnModel().getColumn(2).setPreferredWidth(130); // Mã SKU
        detailTable.getColumnModel().getColumn(3).setPreferredWidth(120); // Đơn giá
        detailTable.getColumnModel().getColumn(4).setPreferredWidth(50);  // SL
        detailTable.getColumnModel().getColumn(5).setPreferredWidth(150); // IMEI
        detailTable.getColumnModel().getColumn(6).setPreferredWidth(130); // Thành tiền
        
        // Cell renderer
        detailTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? CARD_BG : TABLE_ROW_ALT);
                }
                setBorder(new EmptyBorder(0, 10, 0, 10));
                
                // Right align for numbers
                if (column == 0 || column == 3 || column == 4 || column == 6) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                
                return c;
            }
        });
    }
    
    private void loadData() {
        InvoiceDAO invoiceDAO = new InvoiceDAO();
        InvoiceDTO invoice = invoiceDAO.GetInvoiceById(invoiceId);
        List<InvoiceDetailDTO> details = invoiceDAO.GetInvoiceDetails(invoiceId);
        
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        // Update title with more info
        if (invoice != null) {
            setTitle("Phiếu xuất #" + invoiceId + " - " + invoice.getStaffName() + 
                     " - " + (invoice.getCreatedAt() != null ? dateFormat.format(invoice.getCreatedAt()) : ""));
        }
        
        // Load details to table
        tableModel.setRowCount(0);
        int stt = 1;
        double total = 0;
        
        for (InvoiceDetailDTO detail : details) {
            double lineTotal = detail.getPrice() * detail.getQuantity();
            total += lineTotal;
            
            tableModel.addRow(new Object[]{
                stt++,
                detail.getProductName(),
                detail.getSkuCode(),
                currencyFormat.format(detail.getPrice()) + "₫",
                detail.getQuantity(),
                detail.getImeiCode() != null ? detail.getImeiCode() : "-",
                currencyFormat.format(lineTotal) + "₫"
            });
        }
        
        // Add total row
        tableModel.addRow(new Object[]{
            "", "", "", "", "", "TỔNG CỘNG:",
            currencyFormat.format(invoice != null ? invoice.getTotalAmount() : total) + "₫"
        });
    }
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        footer.setBackground(DIALOG_BG);
        footer.setBorder(new EmptyBorder(15, 25, 20, 25));
        
        JButton btnClose = createButton("Đóng", Color.WHITE, DARK_BLUE, false);
        btnClose.addActionListener(e -> dispose());
        
        footer.add(btnClose);
        
        return footer;
    }
    
    private JButton createButton(String text, Color fg, Color bg, boolean outline) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (outline) {
                    g2.setColor(CARD_BG);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.setColor(bg);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 8, 8);
                } else {
                    g2.setColor(getModel().isPressed() ? bg.darker() : bg);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(outline ? bg : fg);
        button.setPreferredSize(new Dimension(120, 42));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
}
