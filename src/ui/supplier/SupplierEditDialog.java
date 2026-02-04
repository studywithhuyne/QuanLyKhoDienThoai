package ui.supplier;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import bus.SupplierBUS;
import dto.SupplierDTO;
import utils.LogHelper;
import static utils.ColorUtil.*;

public class SupplierEditDialog extends JDialog {
    
    // Form fields
    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextArea txtAddress;
    
    // Data
    private int supplierId;
    private String supplierName;
    private String supplierPhone;
    private String supplierEmail;
    private String supplierAddress;
    
    private JButton btnUpdate;
    private JButton btnCancel;
    
    private SupplierPanel supplierPanel;

    private final SupplierBUS supplierBUS = new SupplierBUS();
    
    public SupplierEditDialog(Frame parent, int id, String name, String phone, String email, String address, SupplierPanel supplierPanel) {
        super(parent, "Sửa nhà cung cấp", true);
        this.supplierId = id;
        this.supplierName = name;
        this.supplierPhone = phone;
        this.supplierEmail = email;
        this.supplierAddress = address;
        this.supplierPanel = supplierPanel;
        
        initializeDialog();
        createComponents();
        loadData();
        setVisible(true);
    }
    
    private void initializeDialog() {
        setSize(540, 680);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(DIALOG_BG);
    }
    
    private void createComponents() {
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);
        JPanel formPanel = createForm();
        add(formPanel, BorderLayout.CENTER);
        JPanel footerPanel = createFooter();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CARD_BG);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(20, 25, 20, 25)
        ));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(CARD_BG);
        
        JLabel titleLabel = new JLabel("Sửa nhà cung cấp");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        titlePanel.add(titleLabel);
        
        header.add(titlePanel, BorderLayout.WEST);
        
        return header;
    }
    
    private JPanel createForm() {
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(DIALOG_BG);
        formWrapper.setBorder(new EmptyBorder(25, 25, 15, 25));
        
        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setOpaque(true);
        formCard.setBackground(CARD_BG);
        formCard.putClientProperty("JComponent.roundRect", true);
        formCard.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR, 1, true), new EmptyBorder(25, 25, 25, 25)));
        
        formCard.add(Box.createVerticalGlue());
        
        // ID (readonly)
        txtId = createTextField("");
        txtId.setEditable(false);
        txtId.setBackground(CONTENT_BG);
        formCard.add(createFormGroup("ID nhà cung cấp", txtId));
        formCard.add(Box.createVerticalStrut(18));
        
        // Name
        formCard.add(createFormGroup("Tên nhà cung cấp", txtName = createTextField("Nhập tên nhà cung cấp...")));
        formCard.add(Box.createVerticalStrut(18));
        
        // Phone
        formCard.add(createFormGroup("Số điện thoại", txtPhone = createTextField("Nhập số điện thoại...")));
        formCard.add(Box.createVerticalStrut(18));
        
        // Email
        formCard.add(createFormGroup("Email", txtEmail = createTextField("Nhập email...")));
        formCard.add(Box.createVerticalStrut(18));
        
        // Address
        txtAddress = new JTextArea(4, 20);
        txtAddress.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);
        txtAddress.setBorder(new EmptyBorder(10, 12, 10, 12));
        
        JScrollPane addressScroll = new JScrollPane(txtAddress);
        addressScroll.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        addressScroll.setPreferredSize(new Dimension(0, 100));
        addressScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        formCard.add(createFormGroupWithComponent("Địa chỉ (tùy chọn)", addressScroll));
        
        formCard.add(Box.createVerticalGlue());
        
        formWrapper.add(formCard, BorderLayout.CENTER);
        
        return formWrapper;
    }
    
    private void loadData() {
        txtId.setText(String.valueOf(supplierId));
        txtName.setText(supplierName);
        txtPhone.setText(supplierPhone != null ? supplierPhone : "");
        txtEmail.setText(supplierEmail != null ? supplierEmail : "");
        txtAddress.setText(supplierAddress != null ? supplierAddress : "");
    }
    
    private JPanel createFormGroup(String label, JComponent field) {
        JPanel group = new JPanel();
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        group.setOpaque(false);
        group.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        group.add(lbl);
        group.add(Box.createVerticalStrut(8));
        group.add(field);
        
        return group;
    }
    
    private JPanel createFormGroupWithComponent(String label, JComponent component) {
        JPanel group = new JPanel();
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        group.setOpaque(false);
        group.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        group.add(lbl);
        group.add(Box.createVerticalStrut(8));
        group.add(component);
        
        return group;
    }
    
    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(5, 12, 5, 12)
        ));
        field.putClientProperty("JTextField.placeholderText", placeholder);
        field.putClientProperty("JComponent.roundRect", true);
        
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(new CompoundBorder(
                    new LineBorder(DARK_BLUE, 2, true),
                    new EmptyBorder(4, 11, 4, 11)
                ));
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(new CompoundBorder(
                    new LineBorder(BORDER_COLOR, 1, true),
                    new EmptyBorder(5, 12, 5, 12)
                ));
            }
        });
        
        return field;
    }
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        footer.setBackground(DIALOG_BG);
        footer.setBorder(new EmptyBorder(10, 25, 25, 25));
        
        btnCancel = createButton("Hủy bỏ", TEXT_SECONDARY_DARK, CARD_BG, true);
        btnCancel.addActionListener(e -> dispose());
        
        btnUpdate = createButton("Cập nhật", Color.WHITE, WARNING_COLOR, false);
        btnUpdate.addActionListener(e -> updateSupplier());
        
        footer.add(btnCancel);
        footer.add(btnUpdate);
        
        return footer;
    }
    
    private JButton createButton(String text, Color textColor, Color bgColor, boolean isOutline) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setPreferredSize(new Dimension(isOutline ? 100 : 140, 42));
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
    
    private void updateSupplier() {
        if (txtName.getText().trim().isEmpty()) {
            showError("Vui lòng nhập tên nhà cung cấp!");
            txtName.requestFocus();
            return;
        }

        if (supplierBUS.isNameExistsExcept(txtName.getText().trim(), supplierId)) {
            showError("Tên nhà cung cấp đã tồn tại!");
            txtName.requestFocus();
            return;
        }
        
        SupplierDTO supplier = new SupplierDTO();
        supplier.setID(supplierId);
        supplier.setName(txtName.getText().trim());
        supplier.setPhone(txtPhone.getText().trim());
        supplier.setEmail(txtEmail.getText().trim());
        supplier.setAddress(txtAddress.getText().trim());

        boolean success = supplierBUS.update(supplier);
        
        if (success) {
            LogHelper.logEdit("nhà cung cấp", txtName.getText().trim());
            JOptionPane.showMessageDialog(this, 
                "Cập nhật nhà cung cấp thành công!", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            if (supplierPanel != null) {
                supplierPanel.loadData();
            }
            dispose();
        } else {
            showError("Cập nhật nhà cung cấp thất bại! Tên có thể đã tồn tại.");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
