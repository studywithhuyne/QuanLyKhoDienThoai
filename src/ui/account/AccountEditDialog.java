package ui.account;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

import dao.AccountDAO;
import dto.AccountDTO;
import utils.LogHelper;
import static utils.ColorUtil.*;

public class AccountEditDialog extends JDialog {
    
    // Form fields
    private JTextField txtId;
    private JTextField txtUsername;
    private JTextField txtFullName;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    
    private JButton btnUpdate;
    private JButton btnCancel;
    
    // Data
    private int accountId;
    private String currentUsername;
    private String currentFullName;
    private String currentRole;
    
    private AccountPanel accountPanel;

    public AccountEditDialog(Frame parent, int id, String username, String fullName, String role, AccountPanel accountPanel) {
        super(parent, "Sửa tài khoản", true);
        this.accountId = id;
        this.currentUsername = username;
        this.currentFullName = fullName;
        this.currentRole = role;
        this.accountPanel = accountPanel;
        
        initializeDialog();
        createComponents();
        loadData();
        setVisible(true);
    }
    
    private void initializeDialog() {
        setSize(540, 580);
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
        
        JLabel iconLabel = new JLabel("✏️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(CARD_BG);
        titlePanel.setBorder(new EmptyBorder(0, 15, 0, 0));
        
        JLabel titleLabel = new JLabel("Sửa thông tin tài khoản");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        JLabel subtitleLabel = new JLabel("Cập nhật thông tin tài khoản #" + accountId);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY_DARK);
        
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
        txtId.setBackground(INPUT_DISABLED_BG);
        formCard.add(createFormGroup("ID", txtId));
        formCard.add(Box.createVerticalStrut(18));
        
        // Username
        formCard.add(createFormGroup("Tên đăng nhập", txtUsername = createTextField("Nhập tên đăng nhập...")));
        formCard.add(Box.createVerticalStrut(18));
        
        // Full Name
        formCard.add(createFormGroup("Họ và tên", txtFullName = createTextField("Nhập họ và tên...")));
        formCard.add(Box.createVerticalStrut(18));
        
        // Password (optional for edit)
        formCard.add(createFormGroup("Mật khẩu mới (để trống nếu không đổi)", txtPassword = createPasswordField("Nhập mật khẩu mới...")));
        formCard.add(Box.createVerticalStrut(18));
        
        // Role
        String[] roles = {"Staff", "Admin"};
        formCard.add(createFormGroup("Vai trò", cmbRole = createComboBox(roles)));
        
        formCard.add(Box.createVerticalGlue());
        
        formWrapper.add(formCard, BorderLayout.CENTER);
        
        return formWrapper;
    }
    
    private void loadData() {
        txtId.setText(String.valueOf(accountId));
        txtUsername.setText(currentUsername);
        txtFullName.setText(currentFullName);
        cmbRole.setSelectedItem(currentRole);
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
                if (field.isEditable()) {
                    field.setBorder(new CompoundBorder(
                        new LineBorder(DARK_BLUE, 2, true),
                        new EmptyBorder(4, 11, 4, 11)
                    ));
                }
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
    
    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getPassword().length == 0 && !hasFocus()) {
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
        btnUpdate.addActionListener(e -> updateAccount());
        
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
                    g2.setColor(getModel().isRollover() ? WARNING_HOVER : bgColor);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setPreferredSize(new Dimension(isOutline ? 100 : 140, 42));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void updateAccount() {
        String username = txtUsername.getText().trim();
        String fullName = txtFullName.getText().trim();
        String role = (String) cmbRole.getSelectedItem();
        
        if (username.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đầy đủ thông tin!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        AccountDAO accountDAO = new AccountDAO();
        if (accountDAO.IsUsernameExistsExcept(username, accountId)) {
            JOptionPane.showMessageDialog(this,
                "Tên đăng nhập đã tồn tại!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            txtUsername.requestFocus();
            return;
        }
        
        AccountDTO account = new AccountDTO();
        account.setID(accountId);
        account.setUsername(username);
        account.setFullname(fullName);
        account.setRole(role.equals("Admin") ? "admin" : "staff");
        
        boolean success = accountDAO.EditAccount(account);
        
        if (success) {
            LogHelper.logEdit("tài khoản", username);
            JOptionPane.showMessageDialog(this,
                "Cập nhật tài khoản thành công!",
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
            if (accountPanel != null) {
                accountPanel.loadData();
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Cập nhật tài khoản thất bại!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
