package ui.imei;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import bus.ImeiBUS;
import utils.LogHelper;
import static utils.ColorUtil.*;

public class ImeiDeleteDialog extends JDialog {
    
    private int imeiId;
    private String imeiCode;
    private boolean confirmed = false;
    
    private JButton btnDelete;
    private JButton btnCancel;
    
    private ImeiPanel imeiPanel;
    
    public ImeiDeleteDialog(Frame parent, int id, String imei, ImeiPanel imeiPanel) {
        super(parent, "Xác nhận xóa", true);
        this.imeiId = id;
        this.imeiCode = imei;
        this.imeiPanel = imeiPanel;
        
        initializeDialog();
        createComponents();
        setVisible(true);
    }
    
    private void initializeDialog() {
        setSize(460, 380);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(DIALOG_BG);
    }
    
    private void createComponents() {
        add(createContent(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }
    
    private JPanel createContent() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(DIALOG_BG);
        wrapper.setBorder(new EmptyBorder(15, 30, 10, 30));
        
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(true);
        card.setBackground(CARD_BG);
        card.putClientProperty("JComponent.roundRect", true);
        card.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        card.add(Box.createVerticalGlue());
        
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
        
        JLabel titleLabel = new JLabel("Xóa IMEI?");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel messageLabel = new JLabel("<html><center>Bạn có chắc chắn muốn xóa IMEI<br><b>\"" + imeiCode + "\"</b>?</center></html>");
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
        
        btnDelete = createButton("Xóa IMEI", Color.WHITE, DANGER_RED, false);
        btnDelete.addActionListener(e -> deleteImei());
        
        footer.add(btnCancel);
        footer.add(btnDelete);
        
        return footer;
    }
    
    private JButton createButton(String text, Color textColor, Color bgColor, boolean isOutline) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setPreferredSize(new Dimension(isOutline ? 110 : 130, 44));
        button.setBorderPainted(isOutline);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.putClientProperty("JButton.buttonType", "roundRect");
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (isOutline) {
            button.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        }
        
        return button;
    }
    
    private final ImeiBUS imeiBUS = new ImeiBUS();
    
    private void deleteImei() {
        boolean success = imeiBUS.delete(imeiId);
        
        if (success) {
            LogHelper.logDelete("IMEI", imeiCode);
            confirmed = true;
            JOptionPane.showMessageDialog(this, "Xóa IMEI thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            if (imeiPanel != null) {
                imeiPanel.loadData();
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa IMEI thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
}
