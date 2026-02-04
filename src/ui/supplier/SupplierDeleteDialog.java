package ui.supplier;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import bus.SupplierBUS;
import utils.LogHelper;
import static utils.ColorUtil.*;

public class SupplierDeleteDialog extends JDialog {
    
    // Data
    private int supplierId;
    private String supplierName;
    private boolean confirmed = false;
    
    private JButton btnDelete;
    private JButton btnCancel;
    
    private SupplierPanel supplierPanel;

    private final SupplierBUS supplierBUS = new SupplierBUS();
    
    public SupplierDeleteDialog(Frame parent, int id, String name, SupplierPanel supplierPanel) {
        super(parent, "Xác nhận xóa", true);
        this.supplierId = id;
        this.supplierName = name;
        this.supplierPanel = supplierPanel;
        
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
        JPanel contentPanel = createContent();
        add(contentPanel, BorderLayout.CENTER);
        JPanel footerPanel = createFooter();
        add(footerPanel, BorderLayout.SOUTH);
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
        card.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR, 1, true), new EmptyBorder(30, 30, 30, 30)));
        
        card.add(Box.createVerticalGlue());
        
        // Warning icon
        JLabel iconLabel = new JLabel("⚠");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 25));
        iconLabel.setOpaque(true);
        iconLabel.setBackground(DANGER_BG);
        iconLabel.setPreferredSize(new Dimension(70, 70));
        iconLabel.setMaximumSize(new Dimension(70, 70));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Title
        JLabel titleLabel = new JLabel("Xóa nhà cung cấp?");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Message
        JLabel messageLabel = new JLabel("<html><center>Bạn có chắc chắn muốn xóa nhà cung cấp<br><b>\"" + supplierName + "\"</b>?</center></html>");
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
        
        btnDelete = createButton("Xóa NCC", Color.WHITE, DANGER_RED, false);
        btnDelete.addActionListener(e -> deleteSupplier());
        
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
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.putClientProperty("JButton.buttonType", "roundRect");
        
        if (isOutline) {
            button.setBackground(CARD_BG);
            button.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        } else {
            button.setBackground(bgColor);
            button.setBorder(new EmptyBorder(6, 16, 6, 16));
        }
        
        return button;
    }
    
    private void deleteSupplier() {
        boolean success = supplierBUS.delete(supplierId);
        
        if (success) {
            confirmed = true;
            LogHelper.logDelete("nhà cung cấp", supplierName);
            JOptionPane.showMessageDialog(this, 
                "Xóa nhà cung cấp thành công!", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            if (supplierPanel != null) {
                supplierPanel.loadData();
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Xóa nhà cung cấp thất bại! Có thể đang được sử dụng.", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
}
