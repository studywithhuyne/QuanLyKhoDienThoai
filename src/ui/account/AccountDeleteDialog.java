package ui.account;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import dao.AccountDAO;
import static utils.ColorUtil.*;

public class AccountDeleteDialog extends JDialog {
    
    private JButton btnDelete;
    private JButton btnCancel;
    
    private int accountId;
    private String accountUsername;
    
    private AccountPanel accountPanel;

    public AccountDeleteDialog(Frame parent, int id, String username, AccountPanel accountPanel) {
        super(parent, "Xóa tài khoản", true);
        this.accountId = id;
        this.accountUsername = username;
        this.accountPanel = accountPanel;
        
        initializeDialog();
        createComponents();
        setVisible(true);
    }
    
    private void initializeDialog() {
        setSize(450, 320);
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
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(DIALOG_BG);
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
                g2.setColor(new Color(DANGER_RED.getRed(), DANGER_RED.getGreen(), DANGER_RED.getBlue(), 30));
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
        messageLabel.setForeground(TEXT_SECONDARY_DARK);
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
        footer.setBackground(DIALOG_BG);
        footer.setBorder(new EmptyBorder(0, 30, 25, 30));
        
        btnCancel = createButton("Hủy bỏ", TEXT_SECONDARY_DARK, CARD_BG, true);
        btnCancel.addActionListener(e -> dispose());
        
        btnDelete = createButton("Xóa tài khoản", Color.WHITE, DANGER_RED, false);
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
        button.setPreferredSize(new Dimension(isOutline ? 100 : 140, 42));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void deleteAccount() {
        AccountDAO accountDAO = new AccountDAO();
        boolean success = accountDAO.DeleteAccount(accountId);
        
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Đã xóa tài khoản \"" + accountUsername + "\" thành công!",
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
            if (accountPanel != null) {
                accountPanel.loadData();
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Xóa tài khoản thất bại!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
