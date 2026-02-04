package ui.imei;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.text.ParseException;

import bus.ImeiBUS;
import dto.ImeiDTO;
import utils.LogHelper;
import static utils.ColorUtil.*;

public class ImeiEditDialog extends JDialog {
    
    private JTextField txtId;
    private JTextField txtImei;
    private JComboBox<String> cboStatus;
    private JTextField txtCreatedAt;
    
    private int imeiId;
    private String imeiCode;
    
    private JButton btnUpdate;
    private JButton btnCancel;
    
    private ImeiPanel imeiPanel;
    private ImeiDTO currentImei;

    public ImeiEditDialog(Frame parent, int id, String imei, ImeiPanel imeiPanel) {
        super(parent, "Sửa IMEI", true);
        this.imeiId = id;
        this.imeiCode = imei;
        this.imeiPanel = imeiPanel;
        loadImeiData();
        initializeDialog();
        createComponents();
        loadData();
        setVisible(true);
    }
    
    private final ImeiBUS imeiBUS = new ImeiBUS();
    
    private void loadImeiData() {
        for (ImeiDTO imei : imeiBUS.getAll()) {
            if (imei.getID() == imeiId) {
                currentImei = imei;
                break;
            }
        }
    }
    
    private void initializeDialog() {
        setSize(500, 620);
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
        
        JLabel titleLabel = new JLabel("Sửa IMEI");
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
        
        // ID (readonly)
        txtId = createTextField("");
        txtId.setEditable(false);
        txtId.setBackground(CONTENT_BG);
        formCard.add(createFormGroup("ID", txtId));
        formCard.add(Box.createVerticalStrut(18));
        
        // IMEI
        formCard.add(createFormGroup("Mã IMEI", txtImei = createTextField("Nhập mã IMEI...")));
        formCard.add(Box.createVerticalStrut(18));
        
        // Status
        cboStatus = new JComboBox<>(new String[]{"available", "sold", "warranty", "defective"});
        cboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboStatus.setPreferredSize(new Dimension(Integer.MAX_VALUE, 42));
        cboStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        cboStatus.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null) {
                    switch (value.toString()) {
                        case "available": setText("Còn hàng"); break;
                        case "sold": setText("Đã bán"); break;
                        case "warranty": setText("Bảo hành"); break;
                        case "defective": setText("Lỗi"); break;
                    }
                }
                return this;
            }
        });
        formCard.add(createFormGroup("Trạng thái", cboStatus));
        formCard.add(Box.createVerticalStrut(18));
        
        // Ngày nhập
        formCard.add(createFormGroup("Ngày nhập (dd/MM/yyyy)", txtCreatedAt = createTextField("dd/MM/yyyy")));
        
        formWrapper.add(formCard, BorderLayout.CENTER);
        return formWrapper;
    }
    
    private void loadData() {
        txtId.setText(String.valueOf(imeiId));
        if (currentImei != null) {
            txtImei.setText(currentImei.getImei());
            cboStatus.setSelectedItem(currentImei.getStatus());
            
            if (currentImei.getCreatedAt() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                txtCreatedAt.setText(sdf.format(currentImei.getCreatedAt()));
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
        
        btnUpdate = createButton("Cập nhật", Color.WHITE, WARNING_COLOR, false);
        btnUpdate.addActionListener(e -> updateImei());
        
        footer.add(btnCancel);
        footer.add(btnUpdate);
        
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

    private void updateImei() {
        if (txtImei.getText().trim().isEmpty()) {
            showError("Vui lòng nhập mã IMEI!");
            txtImei.requestFocus();
            return;
        }
        
        if (txtCreatedAt.getText().trim().isEmpty()) {
            showError("Vui lòng nhập ngày nhập!");
            txtCreatedAt.requestFocus();
            return;
        }
        
        // Parse ngày nhập
        Timestamp createdAt;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            java.util.Date date = sdf.parse(txtCreatedAt.getText().trim());
            createdAt = new Timestamp(date.getTime());
        } catch (ParseException e) {
            showError("Ngày nhập không hợp lệ! Vui lòng nhập theo định dạng dd/MM/yyyy");
            txtCreatedAt.requestFocus();
            return;
        }
        
        if (imeiBUS.isImeiExistsExcept(txtImei.getText().trim(), imeiId)) {
            showError("Mã IMEI đã tồn tại!");
            txtImei.requestFocus();
            return;
        }
        
        ImeiDTO imei = new ImeiDTO();
        imei.setID(imeiId);
        imei.setSkuId(currentImei.getSkuId());
        imei.setImei(txtImei.getText().trim());
        imei.setStatus((String) cboStatus.getSelectedItem());
        imei.setCreatedAt(createdAt);
        
        boolean success = imeiBUS.update(imei);
        
        if (success) {
            LogHelper.logEdit("IMEI", txtImei.getText().trim());
            JOptionPane.showMessageDialog(this, "Cập nhật IMEI thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            if (imeiPanel != null) {
                imeiPanel.loadData();
            }
            dispose();
        } else {
            showError("Cập nhật IMEI thất bại! Mã IMEI có thể đã tồn tại.");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
