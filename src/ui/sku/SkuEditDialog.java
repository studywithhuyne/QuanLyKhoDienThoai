package ui.sku;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

import dao.SkuDAO;
import dao.AttributeDAO;
import dao.ProductDAO;
import dto.SkuDTO;
import dto.ProductDTO;
import dto.AttributeDTO;
import dto.AttributeOptionDTO;
import utils.LogHelper;
import static utils.ColorUtil.*;

public class SkuEditDialog extends JDialog {
    
    private JTextField txtId;
    private JTextField txtCode;
    private JTextField txtPrice;
    private JTextField txtStock;
    
    // Attribute selection
    private Map<Integer, JComboBox<AttributeOptionDTO>> attributeComboBoxes;
    private List<AttributeDTO> attributes;
    private Map<Integer, List<AttributeOptionDTO>> attributeOptionsMap;
    private JPanel attributesContainer;
    private AttributeDAO attributeDAO;
    private List<Integer> currentAttributeOptionIds;
    
    private int skuId;
    private String skuCode;
    
    private JButton btnUpdate;
    private JButton btnCancel;
    
    private SkuPanel skuPanel;
    private SkuDTO currentSku;

    public SkuEditDialog(Frame parent, int id, String code, SkuPanel skuPanel) {
        super(parent, "Sửa SKU", true);
        this.skuId = id;
        this.skuCode = code;
        this.skuPanel = skuPanel;
        this.attributeDAO = new AttributeDAO();
        this.attributeComboBoxes = new LinkedHashMap<>();
        this.attributeOptionsMap = new LinkedHashMap<>();
        this.attributes = new ArrayList<>();
        loadSkuData();
        initializeDialog();
        createComponents();
        loadData();
        loadAttributesForSku();
        setVisible(true);
    }
    
    private void loadSkuData() {
        SkuDAO skuDAO = new SkuDAO();
        for (SkuDTO sku : skuDAO.GetAllSku()) {
            if (sku.getID() == skuId) {
                currentSku = sku;
                break;
            }
        }
        // Load current attribute option ids
        currentAttributeOptionIds = skuDAO.GetSkuAttributeOptionIds(skuId);
    }
    
    private void loadAttributesForSku() {
        if (currentSku == null) return;
        
        // Load product to get category
        ProductDAO productDAO = new ProductDAO();
        ProductDTO product = null;
        for (ProductDTO p : productDAO.GetAllProduct()) {
            if (p.getId() == currentSku.getProductId()) {
                product = p;
                break;
            }
        }
        
        if (product == null) return;
        
        // Load attributes for this category
        attributes = attributeDAO.GetAttributesByCategoryId(product.getCategoryId());
        
        for (AttributeDTO attr : attributes) {
            List<AttributeOptionDTO> options = attributeDAO.GetOptionsByAttributeId(attr.getID());
            attributeOptionsMap.put(attr.getID(), options);
        }
        
        // Refresh UI
        refreshAttributesPanel();
    }
    
    private void refreshAttributesPanel() {
        if (attributesContainer == null) return;
        
        // Map tên hiển thị tiếng Việt
        Map<String, String> displayNames = Map.of(
            "ram", "RAM",
            "rom", "ROM", 
            "color", "Màu sắc",
            "length", "Độ dài",
            "wattage", "Công suất",
            "power_bank", "Dung lượng pin",
            "compatibility", "Tương thích"
        );
        
        attributesContainer.removeAll();
        
        JPanel gridPanel = new JPanel(new GridLayout(0, 2, 15, 10));
        gridPanel.setOpaque(false);
        gridPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        for (AttributeDTO attr : attributes) {
            List<AttributeOptionDTO> options = attributeOptionsMap.get(attr.getID());
            if (options != null && !options.isEmpty()) {
                JPanel attrPanel = new JPanel();
                attrPanel.setLayout(new BoxLayout(attrPanel, BoxLayout.Y_AXIS));
                attrPanel.setOpaque(false);
                
                String labelText = displayNames.getOrDefault(attr.getName(), attr.getName());
                JLabel attrLabel = new JLabel(labelText);
                attrLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                attrLabel.setForeground(TEXT_SECONDARY_DARK);
                attrLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                JComboBox<AttributeOptionDTO> combo = new JComboBox<>();
                combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                combo.setPreferredSize(new Dimension(180, 32));
                combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
                combo.addItem(null);
                
                int selectedIndex = 0;
                int index = 1;
                for (AttributeOptionDTO opt : options) {
                    combo.addItem(opt);
                    // Check if this option is currently selected for this SKU
                    if (currentAttributeOptionIds.contains(opt.getID())) {
                        selectedIndex = index;
                    }
                    index++;
                }
                combo.setSelectedIndex(selectedIndex);
                
                combo.setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        if (value == null) {
                            setText("-- Chọn --");
                        } else if (value instanceof AttributeOptionDTO) {
                            setText(((AttributeOptionDTO) value).getValue());
                        }
                        return this;
                    }
                });
                combo.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                attributeComboBoxes.put(attr.getID(), combo);
                
                attrPanel.add(attrLabel);
                attrPanel.add(Box.createVerticalStrut(5));
                attrPanel.add(combo);
                
                gridPanel.add(attrPanel);
            }
        }
        
        if (attributes.isEmpty()) {
            JLabel noAttrLabel = new JLabel("Danh mục này không có thuộc tính");
            noAttrLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            noAttrLabel.setForeground(TEXT_SECONDARY_DARK);
            attributesContainer.add(noAttrLabel);
        } else {
            attributesContainer.add(gridPanel);
        }
        
        attributesContainer.revalidate();
        attributesContainer.repaint();
    }
    
    private void initializeDialog() {
        setSize(500, 700);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(DIALOG_BG);
    }

    private void createComponents() {
        add(createHeader(), BorderLayout.NORTH);
        add(createForm(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
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
        
        JLabel titleLabel = new JLabel("Sửa SKU");
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
        
        // ID (readonly)
        txtId = createTextField("");
        txtId.setEditable(false);
        txtId.setBackground(CONTENT_BG);
        formCard.add(createFormGroup("ID", txtId));
        formCard.add(Box.createVerticalStrut(18));
        
        // Code
        formCard.add(createFormGroup("Mã SKU", txtCode = createTextField("Nhập mã SKU...")));
        formCard.add(Box.createVerticalStrut(18));
        
        // Price
        formCard.add(createFormGroup("Giá", txtPrice = createTextField("Nhập giá...")));
        formCard.add(Box.createVerticalStrut(18));
        
        // Stock
        formCard.add(createFormGroup("Tồn kho", txtStock = createTextField("Nhập số lượng...")));
        formCard.add(Box.createVerticalStrut(18));
        
        // Attributes section
        JPanel attributesPanel = createAttributesPanel();
        formCard.add(attributesPanel);
        
        formWrapper.add(formCard, BorderLayout.CENTER);
        return formWrapper;
    }
    
    private JPanel createAttributesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lbl = new JLabel("Thuộc tính");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(10));
        
        attributesContainer = new JPanel();
        attributesContainer.setLayout(new BoxLayout(attributesContainer, BoxLayout.Y_AXIS));
        attributesContainer.setOpaque(false);
        attributesContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(attributesContainer);
        
        return panel;
    }
    
    private void loadData() {
        txtId.setText(String.valueOf(skuId));
        if (currentSku != null) {
            txtCode.setText(currentSku.getCode());
            txtPrice.setText(String.valueOf((long) currentSku.getPrice()));
            txtStock.setText(String.valueOf(currentSku.getStock()));
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
        btnUpdate.addActionListener(e -> updateSku());
        
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
        button.setPreferredSize(new Dimension(isOutline ? 100 : 130, 42));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private void updateSku() {
        if (txtCode.getText().trim().isEmpty()) {
            showError("Vui lòng nhập mã SKU!");
            txtCode.requestFocus();
            return;
        }
        
        double price;
        int stock;
        try {
            price = Double.parseDouble(txtPrice.getText().trim().replace(",", ""));
        } catch (NumberFormatException e) {
            showError("Giá không hợp lệ!");
            txtPrice.requestFocus();
            return;
        }
        
        try {
            stock = Integer.parseInt(txtStock.getText().trim());
        } catch (NumberFormatException e) {
            showError("Số lượng tồn kho không hợp lệ!");
            txtStock.requestFocus();
            return;
        }
        
        // Collect selected attribute options
        List<Integer> selectedAttributeOptionIds = new ArrayList<>();
        for (Map.Entry<Integer, JComboBox<AttributeOptionDTO>> entry : attributeComboBoxes.entrySet()) {
            AttributeOptionDTO selected = (AttributeOptionDTO) entry.getValue().getSelectedItem();
            if (selected != null) {
                selectedAttributeOptionIds.add(selected.getID());
            }
        }
        
        SkuDAO skuDAO = new SkuDAO();
        
        if (skuDAO.IsCodeExistsExcept(txtCode.getText().trim(), skuId)) {
            showError("Mã SKU đã tồn tại!");
            txtCode.requestFocus();
            return;
        }
        
        SkuDTO sku = new SkuDTO();
        sku.setID(skuId);
        sku.setProductId(currentSku.getProductId());
        sku.setCode(txtCode.getText().trim());
        sku.setPrice(price);
        sku.setStock(stock);
        
        boolean success = skuDAO.EditSku(sku);
        
        if (success) {
            // Update attributes: delete old, add new
            skuDAO.DeleteSkuAttributeOptions(skuId);
            for (int optionId : selectedAttributeOptionIds) {
                skuDAO.AddSkuAttributeOption(skuId, optionId);
            }
            
            LogHelper.logEdit("SKU", txtCode.getText().trim());
            JOptionPane.showMessageDialog(this, "Cập nhật SKU thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            if (skuPanel != null) {
                skuPanel.loadData();
            }
            dispose();
        } else {
            showError("Cập nhật SKU thất bại!");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
