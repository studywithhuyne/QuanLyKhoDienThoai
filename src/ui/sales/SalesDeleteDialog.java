package ui.sales;

import static utils.ColorUtil.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

import dao.InvoiceDAO;

public class SalesDeleteDialog extends JDialog {
    
    // Data
    private int salesId;
    private String salesInfo;
    private boolean confirmed = false;
    
    private JButton btnDelete;
    private JButton btnCancel;
    
    private SalesPanel salesPanel;

    public SalesDeleteDialog(Frame parent, int id, String salesInfo, SalesPanel salesPanel) {
        super(parent, "Xác nhận xóa", true);
        this.salesId = id;
        this.salesInfo = salesInfo;
        this.salesPanel = salesPanel;
        initializeDialog();
        createComponents();
        setVisible(true);
    }
    
    private void initializeDialog() {
        setSize(420, 350);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(DIALOG_BG);
    }
    
    private void createComponents() {
        JPanel contentPanel = createContent();
        add(contentPanel, BorderLayout.CENTER);
        JPanel footerPanel = createFooter();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createContent() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(DIALOG_BG);
        wrapper.setBorder(new EmptyBorder(15, 30, 10, 30));
        
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        card.add(Box.createVerticalGlue());
        
        // Warning icon
        JLabel iconLabel = new JLabel("⚠") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(DANGER_BG);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 25));
        iconLabel.setPreferredSize(new Dimension(70, 70));
        iconLabel.setMaximumSize(new Dimension(70, 70));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Title
        JLabel titleLabel = new JLabel("Xóa hóa đơn?");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Message
        JLabel messageLabel = new JLabel("<html><center>Bạn có chắc chắn muốn xóa<br><b>" + salesInfo + "</b>?</center></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(TEXT_SECONDARY_DARK);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(messageLabel);
        
        card.add(Box.createVerticalGlue());
        
        wrapper.add(card, BorderLayout.CENTER);
        
        return wrapper;
    }
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        footer.setBackground(DIALOG_BG);
        footer.setBorder(new EmptyBorder(0, 30, 30, 30));
        
        btnCancel = createButton("Hủy bỏ", TEXT_SECONDARY_DARK, CARD_BG, true);
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
        
        btnDelete = createButton("Xóa hóa đơn", Color.WHITE, DANGER_RED, false);
        btnDelete.addActionListener(e -> deleteSales());
        
        footer.add(btnCancel);
        footer.add(btnDelete);
        
        return footer;
    }
    
    private JButton createButton(String text, Color textColor, Color bgColor, boolean isOutline) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (isOutline) {
                    g2.setColor(getModel().isRollover() ? CONTENT_BG : bgColor);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                    g2.setColor(BORDER_COLOR);
                    g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                } else {
                    g2.setColor(getModel().isRollover() ? DANGER_HOVER : bgColor);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setPreferredSize(new Dimension(isOutline ? 110 : 150, 44));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void deleteSales() {
        InvoiceDAO invoiceDAO = new InvoiceDAO();
        boolean success = invoiceDAO.DeleteInvoice(salesId);
        
        if (success) {
            confirmed = true;
            JOptionPane.showMessageDialog(this, 
                "Xóa hóa đơn thành công!", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            if (salesPanel != null) {
                salesPanel.loadData();
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Xóa hóa đơn thất bại!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
}
