package ui.invoice;

import static utils.ColorUtil.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import bus.InvoiceBUS;
import dto.InvoiceDTO;
import utils.LogHelper;

public class InvoiceEditDialog extends JDialog {
    
    // Form fields
    private JTextField txtId;
    private JTextField txtEmployee;
    private JTextField txtTotalAmount;
    private JTextField txtDate;
    private JTextArea txtNote;
    
    // Data
    private int salesId;
    
    private JButton btnUpdate;
    private JButton btnCancel;
    
    private InvoicePanel invoicePanel;
    
    public InvoiceEditDialog(Frame parent, int id, String staffName, InvoicePanel invoicePanel) {
        super(parent, "Sửa phiếu xuất", true);
        this.salesId = id;
        this.invoicePanel = invoicePanel;
        
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
        
        JLabel titleLabel = new JLabel("Sửa phiếu xuất");
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
        formCard.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        formCard.add(Box.createVerticalGlue());
        
        // ID (readonly)
        txtId = createTextField("");
        txtId.setEditable(false);
        txtId.setBackground(CONTENT_BG);
        formCard.add(createFormGroup("ID phiếu xuất", txtId));
        formCard.add(Box.createVerticalStrut(18));
        
        // Employee (readonly)
        txtEmployee = createTextField("");
        txtEmployee.setEditable(false);
        txtEmployee.setBackground(CONTENT_BG);
        formCard.add(createFormGroup("Nhân viên bán", txtEmployee));
        formCard.add(Box.createVerticalStrut(18));
        
        // Total Amount
        formCard.add(createFormGroup("Tổng tiền", txtTotalAmount = createTextField("Nhập tổng tiền...")));
        formCard.add(Box.createVerticalStrut(18));
        
        // Date
        formCard.add(createFormGroup("Ngày tạo", txtDate = createTextField("dd/MM/yyyy")));
        formCard.add(Box.createVerticalStrut(18));
        
        // Note
        txtNote = new JTextArea(4, 20);
        txtNote.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNote.setLineWrap(true);
        txtNote.setWrapStyleWord(true);
        txtNote.setBorder(new EmptyBorder(10, 12, 10, 12));
        
        JScrollPane noteScroll = new JScrollPane(txtNote);
        noteScroll.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        noteScroll.setPreferredSize(new Dimension(0, 100));
        noteScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        formCard.add(createFormGroupWithComponent("Ghi chú (tùy chọn)", noteScroll));
        
        formCard.add(Box.createVerticalGlue());
        
        formWrapper.add(formCard, BorderLayout.CENTER);
        
        return formWrapper;
    }
    
    private void loadData() {
        // Load invoice data from database
        InvoiceBUS invoiceBUS = new InvoiceBUS();
        InvoiceDTO invoice = invoiceBUS.getById(salesId);
        
        if (invoice != null) {
            txtId.setText(String.valueOf(invoice.getID()));
            txtEmployee.setText(invoice.getStaffName());
            txtTotalAmount.setText(String.valueOf((long) invoice.getTotalAmount()));
            
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
            txtDate.setText(invoice.getCreatedAt() != null ? dateFormat.format(invoice.getCreatedAt()) : "");
        }
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
        field.putClientProperty("JTextField.placeholderText", placeholder);
        field.setPreferredSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(5, 12, 5, 12)
        ));
        
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
    
    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(Integer.MAX_VALUE, 42));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        combo.setBackground(CARD_BG);
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        combo.setFocusable(false);
        combo.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = super.createArrowButton();
                button.setBackground(CARD_BG);
                button.setBorder(BorderFactory.createEmptyBorder());
                return button;
            }
        });
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(new EmptyBorder(5, 10, 5, 10));
                setBackground(isSelected ? PRIMARY_ALPHA : CARD_BG);
                return this;
            }
        });
        return combo;
    }
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        footer.setBackground(DIALOG_BG);
        footer.setBorder(new EmptyBorder(10, 25, 25, 25));
        
        btnCancel = createButton("Hủy bỏ", TEXT_SECONDARY_DARK, CARD_BG, true);
        btnCancel.addActionListener(e -> dispose());
        
        btnUpdate = createButton("Cập nhật", Color.WHITE, WARNING_COLOR, false);
        btnUpdate.addActionListener(e -> updateSales());
        
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
    
    private void updateSales() {
        if (txtTotalAmount.getText().trim().isEmpty()) {
            showError("Vui lòng nhập tổng tiền!");
            txtTotalAmount.requestFocus();
            return;
        }
        
        double totalAmount;
        try {
            totalAmount = Double.parseDouble(txtTotalAmount.getText().trim().replace(",", ""));
        } catch (NumberFormatException e) {
            showError("Tổng tiền không hợp lệ!");
            txtTotalAmount.requestFocus();
            return;
        }
        
        InvoiceBUS invoiceBUS = new InvoiceBUS();
        InvoiceDTO invoice = invoiceBUS.getById(salesId);
        if (invoice == null) {
            showError("Không tìm thấy phiếu xuất để cập nhật!");
            return;
        }

        invoice.setTotalAmount(totalAmount);
        boolean success = invoiceBUS.update(invoice);
        
        if (success) {
            LogHelper.logEdit("phiếu xuất", "#" + salesId);
            JOptionPane.showMessageDialog(this, 
                "Cập nhật phiếu xuất thành công!", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            if (invoicePanel != null) {
                invoicePanel.loadData();
            }
            dispose();
        } else {
            showError("Cập nhật phiếu xuất thất bại!");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
