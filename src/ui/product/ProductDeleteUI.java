package ui.product;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class ProductDeleteUI extends JDialog {
    
    // Colors - Modern Theme
    private static final Color BACKGROUND = new Color(249, 250, 251);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(17, 24, 39);
    private static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    private static final Color BORDER_COLOR = new Color(229, 231, 235);
    private static final Color DANGER_COLOR = new Color(239, 68, 68);
    private static final Color DANGER_HOVER = new Color(220, 38, 38);
    private static final Color DANGER_BG = new Color(254, 242, 242);
    
    // Product data
    private int productId;
    private String productName;
    private boolean confirmed = false;
    
    // Buttons
    private JButton btnDelete;
    private JButton btnCancel;
    
    public ProductDeleteUI(Frame parent, int id, String name) {
        super(parent, "Xác nhận xóa", true);
        this.productId = id;
        this.productName = name;
        
        initializeDialog();
        createComponents();
        setVisible(true);
    }
    
    private void initializeDialog() {
        setSize(460, 380);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);
    }
    
    private void createComponents() {
        JPanel contentPanel = createContent();
        add(contentPanel, BorderLayout.CENTER);
        
        JPanel footerPanel = createFooter();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createContent() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BACKGROUND);
        wrapper.setBorder(new EmptyBorder(15, 30, 10, 30));
        
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        card.add(Box.createVerticalGlue());
        
        // Warning icon với background tròn
        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(DANGER_BG);
        iconPanel.setPreferredSize(new Dimension(70, 70));
        iconPanel.setMaximumSize(new Dimension(70, 70));
        iconPanel.setMinimumSize(new Dimension(70, 70));
        iconPanel.setLayout(new GridBagLayout());
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel iconLabel = new JLabel("⚠");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setForeground(DANGER_COLOR);
        iconPanel.add(iconLabel);
        
        // Title
        JLabel titleLabel = new JLabel("Xóa sản phẩm?");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Message
        JLabel messageLabel = new JLabel("<html><center>Bạn có chắc chắn muốn xóa sản phẩm<br><b>\"" + productName + "\"</b>?</center></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(TEXT_SECONDARY);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Warning text
        JLabel warningLabel = new JLabel("Hành động này không thể hoàn tác!");
        warningLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        warningLabel.setForeground(DANGER_COLOR);
        warningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconPanel);
        card.add(Box.createVerticalStrut(20));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(messageLabel);
        card.add(Box.createVerticalStrut(15));
        card.add(warningLabel);
        
        card.add(Box.createVerticalGlue());
        
        wrapper.add(card, BorderLayout.CENTER);
        
        return wrapper;
    }
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        footer.setBackground(BACKGROUND);
        footer.setBorder(new EmptyBorder(0, 30, 30, 30));
        
        btnCancel = createButton("Hủy bỏ", TEXT_SECONDARY, CARD_BG, true);
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
        
        btnDelete = createButton("Xóa sản phẩm", Color.WHITE, DANGER_COLOR, false);
        btnDelete.addActionListener(e -> deleteProduct());
        
        footer.add(btnCancel);
        footer.add(btnDelete);
        
        return footer;
    }
    
    private JButton createButton(String text, Color textColor, Color bgColor, boolean isOutline) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setPreferredSize(new Dimension(isOutline ? 110 : 150, 44));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        if (isOutline) {
            button.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        } else {
            button.setBorder(new EmptyBorder(8, 16, 8, 16));
            button.setBorderPainted(false);
        }
        
        // Hover effect
        Color hoverColor = isOutline ? new Color(243, 244, 246) : DANGER_HOVER;
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void deleteProduct() {
        confirmed = true;
        JOptionPane.showMessageDialog(this, 
            "Xóa sản phẩm thành công!", 
            "Thành công", 
            JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public int getProductId() {
        return productId;
    }
}
