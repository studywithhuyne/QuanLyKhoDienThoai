package ui.imei;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

import dao.ImeiDAO;
import dao.SkuDAO;
import dao.ImportReceiptDAO;
import dto.ImeiDTO;
import dto.SkuDTO;
import dto.ImportReceiptDTO;
import static utils.ColorUtil.*;

public class ImeiAddDialog extends JDialog {
    
    private JComboBox<SkuDTO> cboSku;
    private JComboBox<ImportReceiptDTO> cboImportReceipt;
    private JTextField txtImei;
    
    private JButton btnSave;
    private JButton btnCancel;
    
    private ImeiPanel imeiPanel;
    private List<SkuDTO> skus;
    private List<ImportReceiptDTO> receipts;

    public ImeiAddDialog(Frame parent, ImeiPanel imeiPanel) {
        super(parent, "Thêm IMEI", true);
        this.imeiPanel = imeiPanel;
        loadData();
        initializeDialog();
        createComponents();
        setVisible(true);
    }
    
    private void loadData() {
        SkuDAO skuDAO = new SkuDAO();
        skus = skuDAO.GetAllSku();
        ImportReceiptDAO receiptDAO = new ImportReceiptDAO();
        receipts = receiptDAO.GetAllImportReceipt();
    }
    
    private void initializeDialog() {
        setSize(500, 520);
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
        
        JLabel titleLabel = new JLabel("Thêm IMEI");
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
        
        // SKU ComboBox
        cboSku = new JComboBox<>();
        cboSku.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboSku.setPreferredSize(new Dimension(Integer.MAX_VALUE, 42));
        cboSku.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        for (SkuDTO sku : skus) {
            cboSku.addItem(sku);
        }
        cboSku.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof SkuDTO) {
                    SkuDTO sku = (SkuDTO) value;
                    setText(sku.getCode() + " - " + sku.getProductName());
                }
                return this;
            }
        });
        formCard.add(createFormGroup("SKU", cboSku));
        formCard.add(Box.createVerticalStrut(18));
        
        // Import Receipt ComboBox
        cboImportReceipt = new JComboBox<>();
        cboImportReceipt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboImportReceipt.setPreferredSize(new Dimension(Integer.MAX_VALUE, 42));
        cboImportReceipt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        for (ImportReceiptDTO receipt : receipts) {
            cboImportReceipt.addItem(receipt);
        }
        cboImportReceipt.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ImportReceiptDTO) {
                    ImportReceiptDTO r = (ImportReceiptDTO) value;
                    setText("#" + r.getID() + " - " + r.getSupplierName());
                }
                return this;
            }
        });
        formCard.add(createFormGroup("Phiếu nhập", cboImportReceipt));
        formCard.add(Box.createVerticalStrut(18));
        
        // IMEI
        formCard.add(createFormGroup("Mã IMEI", txtImei = createTextField("Nhập mã IMEI...")));
        
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
        
        btnSave = createButton("Lưu IMEI", Color.WHITE, DARK_BLUE, false);
        btnSave.addActionListener(e -> saveImei());
        
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
        button.setPreferredSize(new Dimension(isOutline ? 100 : 130, 42));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private void saveImei() {
        if (cboSku.getSelectedItem() == null) {
            showError("Vui lòng chọn SKU!");
            return;
        }
        
        if (cboImportReceipt.getSelectedItem() == null) {
            showError("Vui lòng chọn phiếu nhập!");
            return;
        }
        
        if (txtImei.getText().trim().isEmpty()) {
            showError("Vui lòng nhập mã IMEI!");
            txtImei.requestFocus();
            return;
        }
        
        SkuDTO selectedSku = (SkuDTO) cboSku.getSelectedItem();
        ImportReceiptDTO selectedReceipt = (ImportReceiptDTO) cboImportReceipt.getSelectedItem();
        
        ImeiDAO imeiDAO = new ImeiDAO();
        
        if (imeiDAO.IsImeiExists(txtImei.getText().trim())) {
            showError("Mã IMEI đã tồn tại!");
            txtImei.requestFocus();
            return;
        }
        
        ImeiDTO imei = new ImeiDTO();
        imei.setSkuId(selectedSku.getID());
        imei.setImportReceiptId(selectedReceipt.getID());
        imei.setImei(txtImei.getText().trim());
        imei.setStatus("available");
        
        boolean success = imeiDAO.AddImei(imei);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Thêm IMEI thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            if (imeiPanel != null) {
                imeiPanel.loadData();
            }
            dispose();
        } else {
            showError("Thêm IMEI thất bại!");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
