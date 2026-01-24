package ui.import_;

import static utils.ColorUtil.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dao.AccountDAO;
import dao.ImportReceiptDAO;
import dao.SupplierDAO;
import dto.AccountDTO;
import dto.ImportReceiptDTO;
import dto.SupplierDTO;

public class ImportAddDialog extends JDialog {
    
    // Form fields
    private JComboBox<SupplierDTO> cmbSupplier;
    private JComboBox<AccountDTO> cmbEmployee;
    private JTextField txtTotalAmount;
    private JTextField txtDate;
    private JTextArea txtNote;
    
    private JButton btnSave;
    private JButton btnCancel;
    
    private ImportPanel importPanel;

    public ImportAddDialog(Frame parent, ImportPanel importPanel) {
        super(parent, "Thêm phiếu nhập kho", true);
        this.importPanel = importPanel;
        initializeDialog();
        createComponents();
        loadComboBoxData();
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
        
        JLabel titleLabel = new JLabel("Thêm phiếu nhập kho");
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
        
        JPanel formCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
            }
        };
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setOpaque(false);
        formCard.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        formCard.add(Box.createVerticalGlue());

        // Supplier
        cmbSupplier = new JComboBox<>();
        styleComboBox(cmbSupplier);
        formCard.add(createFormGroup("Nhà cung cấp", cmbSupplier));
        formCard.add(Box.createVerticalStrut(18));
        
        // Employee
        cmbEmployee = new JComboBox<>();
        styleComboBox(cmbEmployee);
        formCard.add(createFormGroup("Nhân viên nhập", cmbEmployee));
        formCard.add(Box.createVerticalStrut(18));
        
        // Total Amount
        formCard.add(createFormGroup("Tổng tiền", txtTotalAmount = createTextField("Nhập tổng tiền...")));
        formCard.add(Box.createVerticalStrut(18));
        
        // Date
        txtDate = createTextField("");
        txtDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        formCard.add(createFormGroup("Ngày tạo", txtDate));
        formCard.add(Box.createVerticalStrut(18));
        
        // Note
        txtNote = new JTextArea(4, 20);
        txtNote.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNote.setLineWrap(true);
        txtNote.setWrapStyleWord(true);
        txtNote.setBorder(new EmptyBorder(10, 12, 10, 12));
        
        JScrollPane noteScroll = new JScrollPane(txtNote);
        noteScroll.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        noteScroll.setPreferredSize(new Dimension(0, 120));
        noteScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        formCard.add(createFormGroupWithComponent("Ghi chú (tùy chọn)", noteScroll));
        
        formCard.add(Box.createVerticalGlue());
        
        formWrapper.add(formCard, BorderLayout.CENTER);
        
        return formWrapper;
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
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(TEXT_SECONDARY_DARK);
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    g2.drawString(placeholder, 12, 26);
                    g2.dispose();
                }
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
    
    private <T> void styleComboBox(JComboBox<T> combo) {
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
    }
    
    private void loadComboBoxData() {
        // Load suppliers
        SupplierDAO supplierDAO = new SupplierDAO();
        List<SupplierDTO> suppliers = supplierDAO.GetAllSupplier();
        cmbSupplier.removeAllItems();
        for (SupplierDTO supplier : suppliers) {
            cmbSupplier.addItem(supplier);
        }
        
        // Load employees
        AccountDAO accountDAO = new AccountDAO();
        List<AccountDTO> accounts = accountDAO.GetAllAccount();
        cmbEmployee.removeAllItems();
        for (AccountDTO account : accounts) {
            cmbEmployee.addItem(account);
        }
    }
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        footer.setBackground(DIALOG_BG);
        footer.setBorder(new EmptyBorder(10, 25, 25, 25));
        
        btnCancel = createButton("Hủy bỏ", TEXT_SECONDARY_DARK, CARD_BG, true);
        btnCancel.addActionListener(e -> dispose());
        
        btnSave = createButton("Lưu phiếu nhập", Color.WHITE, DARK_BLUE, false);
        btnSave.addActionListener(e -> saveImport());
        
        footer.add(btnCancel);
        footer.add(btnSave);
        
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
                    g2.setColor(getModel().isRollover() ? PRIMARY_HOVER : bgColor);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setPreferredSize(new Dimension(isOutline ? 100 : 160, 42));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private void saveImport() {
        SupplierDTO selectedSupplier = (SupplierDTO) cmbSupplier.getSelectedItem();
        AccountDTO selectedEmployee = (AccountDTO) cmbEmployee.getSelectedItem();
        
        if (selectedSupplier == null) {
            showError("Vui lòng chọn nhà cung cấp!");
            return;
        }
        
        if (selectedEmployee == null) {
            showError("Vui lòng chọn nhân viên!");
            return;
        }
        
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
        
        // Create DTO and save to database
        ImportReceiptDTO receipt = new ImportReceiptDTO();
        receipt.setSupplierId(selectedSupplier.getID());
        receipt.setStaffId(selectedEmployee.getID());
        receipt.setTotalAmount(totalAmount);
        
        ImportReceiptDAO importDAO = new ImportReceiptDAO();
        int newId = importDAO.AddImportReceipt(receipt);
        
        if (newId > 0) {
            JOptionPane.showMessageDialog(this, "Thêm phiếu nhập thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            if (importPanel != null) {
                importPanel.loadData();
            }
            dispose();
        } else {
            showError("Thêm phiếu nhập thất bại!");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
