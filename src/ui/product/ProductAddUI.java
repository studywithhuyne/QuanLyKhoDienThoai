package ui.product;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class ProductAddUI extends JDialog {
    
    // Colors - Modern Theme
    private static final Color PRIMARY_COLOR = new Color(99, 102, 241);
    private static final Color PRIMARY_HOVER = new Color(129, 140, 248);
    private static final Color BACKGROUND = new Color(249, 250, 251);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(17, 24, 39);
    private static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    private static final Color BORDER_COLOR = new Color(229, 231, 235);
    
    // Form fields
    private JTextField txtName;
    private JComboBox<String> cmbBrand;
    private JComboBox<String> cmbCategory;
    private JTextArea txtDescription; 
    
    private JButton btnSave;
    private JButton btnCancel;

    public ProductAddUI(Frame parent) {
        super(parent, "Thêm sản phẩm mới", true);
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
        
        JLabel iconLabel = new JLabel("📱");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(CARD_BG);
        titlePanel.setBorder(new EmptyBorder(0, 15, 0, 0));
        
        JLabel titleLabel = new JLabel("Thêm sản phẩm mới");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        JLabel subtitleLabel = new JLabel("Nhập thông tin sản phẩm bên dưới");
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
        combo.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        return combo;
    }
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        footer.setBackground(BACKGROUND);
        footer.setBorder(new EmptyBorder(10, 25, 25, 25));
        
        btnCancel = createButton("Hủy bỏ", TEXT_SECONDARY, CARD_BG, true);
        btnCancel.addActionListener(e -> dispose());
        
        btnSave = createButton("Lưu sản phẩm", Color.WHITE, PRIMARY_COLOR, false);
        btnSave.addActionListener(e -> saveProduct());
        
        footer.add(btnCancel);
        footer.add(btnSave);
        
        return footer;
    }
    
    private JButton createButton(String text, Color textColor, Color bgColor, boolean isOutline) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setPreferredSize(new Dimension(isOutline ? 100 : 160, 42));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        if (isOutline) {
            button.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        } else {
            button.setBorder(new EmptyBorder(8, 16, 8, 16));
            button.setBorderPainted(false);
        }
        
        // Hover effect đơn giản
        Color hoverColor = isOutline ? new Color(243, 244, 246) : PRIMARY_HOVER;
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

    private void saveProduct() {
        if (txtName.getText().trim().isEmpty()) {
            showError("Vui lòng nhập tên sản phẩm!");
            txtName.requestFocus();
            return;
        }
        JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
