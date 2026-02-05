package ui.imei;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import bus.ImeiBUS;
import bus.SkuBUS;
import bus.ImportReceiptBUS;
import dto.ImeiDTO;
import dto.SkuDTO;
import dto.ImportReceiptDTO;
import utils.LogHelper;
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
    
    private final ImeiBUS imeiBUS = new ImeiBUS();
    private final SkuBUS skuBUS = new SkuBUS();
    private final ImportReceiptBUS importReceiptBUS = new ImportReceiptBUS();
    
    private void loadData() {
        skus = skuBUS.getAll();
        receipts = importReceiptBUS.getAll();
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
        
        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setOpaque(true);
        formCard.setBackground(CARD_BG);
        formCard.putClientProperty("JComponent.roundRect", true);
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
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setPreferredSize(new Dimension(isOutline ? 100 : 130, 42));
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
        
        if (imeiBUS.isImeiExists(txtImei.getText().trim())) {
            showError("Mã IMEI đã tồn tại!");
            txtImei.requestFocus();
            return;
        }
        
        ImeiDTO imei = new ImeiDTO();
        imei.setSkuId(selectedSku.getID());
        imei.setImportReceiptId(selectedReceipt.getID());
        imei.setImei(txtImei.getText().trim());
        imei.setStatus("available");
        
        boolean success = imeiBUS.add(imei);
        
        if (success) {
            LogHelper.logAdd("IMEI", txtImei.getText().trim());
            JOptionPane.showMessageDialog(this, "Thêm IMEI thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            if (imeiPanel != null) {
                imeiPanel.loadData();
            }
            dispose();
        } else {
            showError("Thêm IMEI thất bại! Mã IMEI có thể đã tồn tại.");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
