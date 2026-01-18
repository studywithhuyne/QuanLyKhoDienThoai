package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImportAddUI extends JDialog {
    
    // Colors - Modern Theme
    private static final Color PRIMARY_COLOR = new Color(99, 102, 241);
    private static final Color PRIMARY_HOVER = new Color(129, 140, 248);
    private static final Color BACKGROUND = new Color(249, 250, 251);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(17, 24, 39);
    private static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    private static final Color BORDER_COLOR = new Color(229, 231, 235);
    
    // Form fields
    private JComboBox<String> cmbSupplier;
    private JComboBox<String> cmbEmployee;
    private JTextField txtTotalAmount;
    private JTextField txtDate;
    private JTextArea txtNote;
    
    private JButton btnSave;
    private JButton btnCancel;

    public ImportAddUI(Frame parent) {
        super(parent, "Thêm phiếu nhập kho", true);
        initializeDialog();
        createComponents();
        setVisible(true);
    }
    
    private void initializeDialog() {
        setSize(540, 680);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);
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
        
        JLabel iconLabel = new JLabel("📥");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(CARD_BG);
        titlePanel.setBorder(new EmptyBorder(0, 15, 0, 0));
        
        JLabel titleLabel = new JLabel("Thêm phiếu nhập kho");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        JLabel subtitleLabel = new JLabel("Nhập thông tin phiếu nhập bên dưới");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(subtitleLabel);
        
        JPanel leftSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftSection.setBackground(CARD_BG);
        leftSection.add(iconLabel);
        leftSection.add(titlePanel);
        
        header.add(leftSection, BorderLayout.WEST);
        return header;
    }
    
    private JPanel createForm() {
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(BACKGROUND);
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
        String[] suppliers = {"FPT Synnex", "Viettel Store", "CellphoneS B2B", "Anker Vietnam", "Baseus Official", "Ugreen Vietnam"};
        formCard.add(createFormGroup("Nhà cung cấp", cmbSupplier = createComboBox(suppliers)));
        formCard.add(Box.createVerticalStrut(18));
        
        // Employee
        String[] employees = {"Admin", "Jerry"};
        formCard.add(createFormGroup("Nhân viên nhập", cmbEmployee = createComboBox(employees)));
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
                    g2.setColor(TEXT_SECONDARY);
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
                    new LineBorder(PRIMARY_COLOR, 2, true),
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
        // Custom UI to remove focus border
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
                setBackground(isSelected ? new Color(99, 102, 241, 30) : CARD_BG);
                return this;
            }
        });
        return combo;
    }
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        footer.setBackground(BACKGROUND);
        footer.setBorder(new EmptyBorder(10, 25, 25, 25));
        
        btnCancel = createButton("Hủy bỏ", TEXT_SECONDARY, CARD_BG, true);
        btnCancel.addActionListener(e -> dispose());
        
        btnSave = createButton("Lưu phiếu nhập", Color.WHITE, PRIMARY_COLOR, false);
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
                    g2.setColor(getModel().isRollover() ? new Color(243, 244, 246) : bgColor);
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
        if (txtTotalAmount.getText().trim().isEmpty()) {
            showError("Vui lòng nhập tổng tiền!");
            txtTotalAmount.requestFocus();
            return;
        }
        JOptionPane.showMessageDialog(this, "Thêm phiếu nhập thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
