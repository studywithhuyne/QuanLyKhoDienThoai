package ui.product;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class ProductEditDialog extends JDialog {
    
    // Colors - Modern Theme
    private static final Color PRIMARY_COLOR = new Color(99, 102, 241);
    private static final Color PRIMARY_HOVER = new Color(129, 140, 248);
    private static final Color BACKGROUND = new Color(249, 250, 251);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(17, 24, 39);
    private static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    private static final Color BORDER_COLOR = new Color(229, 231, 235);
    private static final Color WARNING_COLOR = new Color(251, 191, 36);
    private static final Color WARNING_HOVER = new Color(245, 158, 11);
    private static final Color DANGER_COLOR = new Color(239, 68, 68);
    
    // Form fields
    private JTextField txtId;
    private JTextField txtName;
    private JComboBox<String> cmbBrand;
    private JComboBox<String> cmbCategory;
    private JTextArea txtDescription;
    
    // Product data
    private int productId;
    private String productName;
    private String productBrand;
    private String productCategory;
    
    // Buttons
    private JButton btnUpdate;
    private JButton btnCancel;
    
    public ProductEditDialog(Frame parent, int id, String name, String brand, String category) {
        super(parent, "Sửa thông tin sản phẩm", true);
        this.productId = id;
        this.productName = name;
        this.productBrand = brand;
        this.productCategory = category;
        
        initializeDialog();
        createComponents();
        loadProductData();
        setVisible(true);
    }
    
    private void initializeDialog() {
        setSize(540, 730);
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
        
        JLabel iconLabel = new JLabel("✏️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(CARD_BG);
        titlePanel.setBorder(new EmptyBorder(0, 15, 0, 0));
        
        JLabel titleLabel = new JLabel("Sửa thông tin sản phẩm");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        JLabel subtitleLabel = new JLabel("Chỉnh sửa thông tin sản phẩm #" + productId);
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
        
        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(CARD_BG);
        formCard.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        formCard.add(Box.createVerticalGlue());
        
        // Product ID (readonly)
        txtId = createTextField("");
        txtId.setEditable(false);
        txtId.setBackground(new Color(243, 244, 246));
        formCard.add(createFormGroup("ID sản phẩm", txtId));
        formCard.add(Box.createVerticalStrut(18));
        
        // Product Name
        formCard.add(createFormGroup("Tên sản phẩm", txtName = createTextField("Nhập tên sản phẩm...")));
        formCard.add(Box.createVerticalStrut(18));
        
        // Brand
        String[] brands = {"Apple", "Samsung", "Xiaomi", "Oppo", "Anker", "Baseus", "Belkin", "Sony", "Ugreen"};
        formCard.add(createFormGroup("Thương hiệu", cmbBrand = createComboBox(brands)));
        formCard.add(Box.createVerticalStrut(18));
        
        // Category
        String[] categories = {"Điện thoại", "Cáp sạc", "Cường lực", "Sạc dự phòng", "Củ sạc", "Loa"};
        formCard.add(createFormGroup("Danh mục", cmbCategory = createComboBox(categories)));
        formCard.add(Box.createVerticalStrut(18));
        
        // Description
        txtDescription = new JTextArea(6, 20);
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setBorder(new EmptyBorder(10, 12, 10, 12));
        
        JScrollPane descScroll = new JScrollPane(txtDescription);
        descScroll.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        descScroll.setPreferredSize(new Dimension(0, 150));
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        formCard.add(createFormGroupWithComponent("Mô tả (tùy chọn)", descScroll));
        
        formCard.add(Box.createVerticalGlue());
        
        formWrapper.add(formCard, BorderLayout.CENTER);
        
        return formWrapper;
    }
    
    private void loadProductData() {
        txtId.setText(String.valueOf(productId));
        txtName.setText(productName);
        cmbBrand.setSelectedItem(productBrand);
        cmbCategory.setSelectedItem(productCategory);
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
        field.setForeground(TEXT_PRIMARY);
        field.putClientProperty("JTextField.placeholderText", placeholder);
        
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
        
        btnUpdate = createButton("Cập nhật", Color.WHITE, WARNING_COLOR, false);
        btnUpdate.addActionListener(e -> updateProduct());
        
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
        Color hoverColor = isOutline ? new Color(243, 244, 246) : WARNING_HOVER;
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
    
    private void updateProduct() {
        if (txtName.getText().trim().isEmpty()) {
            showError("Vui lòng nhập tên sản phẩm!");
            txtName.requestFocus();
            return;
        }
        
        JOptionPane.showMessageDialog(this, 
            "Cập nhật sản phẩm thành công!", 
            "Thành công", 
            JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    public int getProductId() { return productId; }
    public String getProductName() { return txtName.getText().trim(); }
    public String getBrand() { return (String) cmbBrand.getSelectedItem(); }
    public String getCategory() { return (String) cmbCategory.getSelectedItem(); }
    public String getDescription() { return txtDescription.getText().trim(); }
}
