package ui.attribute;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

import dao.AttributeDAO;
import dto.AttributeDTO;
import dto.AttributeOptionDTO;
import static utils.ColorUtil.*;

public class AttributeEditDialog extends JDialog {
    
    // Form fields
    private JTextField txtId;
    private JComboBox<AttributeDTO> cboAttribute;
    private JTextField txtValue;
    
    // Data
    private int optionId;
    private String attributeName;
    private String optionValue;
    
    private JButton btnUpdate;
    private JButton btnCancel;
    
    private AttributePanel attributePanel;
    private List<AttributeDTO> attributes;
    
    public AttributeEditDialog(Frame parent, int id, String attributeName, String value, AttributePanel attributePanel) {
        super(parent, "Sửa giá trị thuộc tính", true);
        this.optionId = id;
        this.attributeName = attributeName;
        this.optionValue = value;
        this.attributePanel = attributePanel;
        
        loadAttributes();
        initializeDialog();
        createComponents();
        loadData();
        setVisible(true);
    }
    
    private void loadAttributes() {
        AttributeDAO attributeDAO = new AttributeDAO();
        attributes = attributeDAO.GetAllAttribute();
    }
    
    private void initializeDialog() {
        setSize(480, 520);
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
        
        JLabel titleLabel = new JLabel("Sửa giá trị thuộc tính");
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
        
        // ID (readonly)
        txtId = createTextField("");
        txtId.setEditable(false);
        txtId.setBackground(CONTENT_BG);
        formCard.add(createFormGroup("ID", txtId));
        formCard.add(Box.createVerticalStrut(18));
        
        // Attribute ComboBox
        cboAttribute = new JComboBox<>();
        cboAttribute.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboAttribute.setPreferredSize(new Dimension(Integer.MAX_VALUE, 42));
        cboAttribute.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        for (AttributeDTO attr : attributes) {
            cboAttribute.addItem(attr);
        }
        cboAttribute.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof AttributeDTO) {
                    setText(((AttributeDTO) value).getName());
                }
                return this;
            }
        });
        formCard.add(createFormGroup("Thuộc tính", cboAttribute));
        formCard.add(Box.createVerticalStrut(18));
        
        // Value
        formCard.add(createFormGroup("Giá trị", txtValue = createTextField("Nhập giá trị...")));
        
        formCard.add(Box.createVerticalGlue());
        
        formWrapper.add(formCard, BorderLayout.CENTER);
        
        return formWrapper;
    }
    
    private void loadData() {
        txtId.setText(String.valueOf(optionId));
        txtValue.setText(optionValue);
        
        // Select the correct attribute in combo box
        for (int i = 0; i < cboAttribute.getItemCount(); i++) {
            AttributeDTO attr = cboAttribute.getItemAt(i);
            if (attr.getName().equals(attributeName)) {
                cboAttribute.setSelectedIndex(i);
                break;
            }
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
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        footer.setBackground(DIALOG_BG);
        footer.setBorder(new EmptyBorder(10, 25, 25, 25));
        
        btnCancel = createButton("Hủy bỏ", TEXT_SECONDARY_DARK, CARD_BG, true);
        btnCancel.addActionListener(e -> dispose());
        
        btnUpdate = createButton("Cập nhật", Color.WHITE, WARNING_COLOR, false);
        btnUpdate.addActionListener(e -> updateAttribute());
        
        footer.add(btnCancel);
        footer.add(btnUpdate);
        
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
                    Color hoverColor = new Color(
                        Math.min(bgColor.getRed() + 20, 255),
                        Math.min(bgColor.getGreen() + 20, 255),
                        Math.min(bgColor.getBlue() + 20, 255)
                    );
                    g2.setColor(getModel().isRollover() ? hoverColor : bgColor);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setPreferredSize(new Dimension(isOutline ? 100 : 120, 42));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void updateAttribute() {
        if (cboAttribute.getSelectedItem() == null) {
            showError("Vui lòng chọn thuộc tính!");
            return;
        }
        
        if (txtValue.getText().trim().isEmpty()) {
            showError("Vui lòng nhập giá trị!");
            txtValue.requestFocus();
            return;
        }
        
        AttributeDTO selectedAttr = (AttributeDTO) cboAttribute.getSelectedItem();
        
        AttributeOptionDTO option = new AttributeOptionDTO();
        option.setID(optionId);
        option.setAttributeId(selectedAttr.getID());
        option.setValue(txtValue.getText().trim());
        
        AttributeDAO attributeDAO = new AttributeDAO();
        boolean success = attributeDAO.EditAttributeOption(option);
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Cập nhật thành công!", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            if (attributePanel != null) {
                attributePanel.loadData();
            }
            dispose();
        } else {
            showError("Cập nhật thất bại!");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
