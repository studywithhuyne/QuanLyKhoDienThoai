package ui.account;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import bus.AccountBUS;
import utils.LogHelper;
import static utils.ColorUtil.*;

public class AccountDeleteDialog extends JDialog {
    
    private JButton btnDelete;
    private JButton btnCancel;
    
    private int accountId;
    private String accountUsername;
    
    private AccountPanel accountPanel;

    private final AccountBUS accountBUS = new AccountBUS();

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
        setSize(460, 360);
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
        
        JPanel contentCard = new JPanel();
        contentCard.setLayout(new BoxLayout(contentCard, BoxLayout.Y_AXIS));
        contentCard.setOpaque(true);
        contentCard.setBackground(CARD_BG);
        contentCard.putClientProperty("JComponent.roundRect", true);
        contentCard.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
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
        
        contentCard.add(Box.createVerticalStrut(5));
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
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setPreferredSize(new Dimension(isOutline ? 100 : 140, 42));
        button.putClientProperty("JButton.buttonType", "roundRect");
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        if (isOutline) {
            button.setBackground(CARD_BG);
            button.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        } else {
            button.setBackground(bgColor);
            button.setBorder(new EmptyBorder(6, 16, 6, 16));
        }
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void deleteAccount() {
        boolean success = accountBUS.delete(accountId);
        
        if (success) {
            LogHelper.logDelete("tài khoản", accountUsername);
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
