package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AccountDeleteUI extends JDialog {
    
    // Colors - Modern Theme
    private static final Color DANGER_COLOR = new Color(239, 68, 68);
    private static final Color DANGER_HOVER = new Color(220, 38, 38);
    private static final Color BACKGROUND = new Color(249, 250, 251);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(17, 24, 39);
    private static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    private static final Color BORDER_COLOR = new Color(229, 231, 235);
    
    private JButton btnDelete;
    private JButton btnCancel;
    
    private int accountId;
    private String accountUsername;

    public AccountDeleteUI(Frame parent, int id, String username) {
        super(parent, "Xóa tài khoản", true);
        this.accountId = id;
        this.accountUsername = username;
        
        initializeDialog();
        createComponents();
        setVisible(true);
    }
    
    private void initializeDialog() {
        setSize(450, 320);
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
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(BACKGROUND);
        contentWrapper.setBorder(new EmptyBorder(30, 30, 20, 30));
        
        JPanel contentCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
            }
        };
        contentCard.setLayout(new BoxLayout(contentCard, BoxLayout.Y_AXIS));
        contentCard.setOpaque(false);
        contentCard.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Warning Icon
        JLabel iconLabel = new JLabel("⚠️") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(DANGER_COLOR.getRed(), DANGER_COLOR.getGreen(), DANGER_COLOR.getBlue(), 30));
                g2.fillOval(10, 0, 60, 60);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconLabel.setPreferredSize(new Dimension(80, 60));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Title
        JLabel titleLabel = new JLabel("Xác nhận xóa tài khoản?");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Message
        JLabel messageLabel = new JLabel("<html><center>Bạn có chắc chắn muốn xóa tài khoản<br><b>\"" + accountUsername + "\"</b> (ID: " + accountId + ")?<br>Hành động này không thể hoàn tác.</center></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(TEXT_SECONDARY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        contentCard.add(iconLabel);
        contentCard.add(Box.createVerticalStrut(15));
        contentCard.add(titleLabel);
        contentCard.add(Box.createVerticalStrut(12));
        contentCard.add(messageLabel);
        
        contentWrapper.add(contentCard, BorderLayout.CENTER);
        
        return contentWrapper;
    }
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        footer.setBackground(BACKGROUND);
        footer.setBorder(new EmptyBorder(0, 30, 25, 30));
        
        btnCancel = createButton("Hủy bỏ", TEXT_SECONDARY, CARD_BG, true);
        btnCancel.addActionListener(e -> dispose());
        
        btnDelete = createButton("Xóa tài khoản", Color.WHITE, DANGER_COLOR, false);
        btnDelete.addActionListener(e -> deleteAccount());
        
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
                    g2.setColor(getModel().isRollover() ? new Color(243, 244, 246) : bgColor);
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
        button.setPreferredSize(new Dimension(isOutline ? 100 : 140, 42));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void deleteAccount() {
        // TODO: Delete from database
        JOptionPane.showMessageDialog(this,
            "Đã xóa tài khoản \"" + accountUsername + "\" thành công!",
            "Thành công",
            JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
