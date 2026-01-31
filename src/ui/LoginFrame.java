package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

import com.formdev.flatlaf.FlatLightLaf;

import dao.AccountDAO;
import dto.AccountDTO;
import utils.SessionManager;
import utils.LogHelper;
import static utils.ColorUtil.*;

public class LoginFrame extends JFrame {
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblError;
    
    public LoginFrame() {
        initializeFrame();
        createComponents();
        setVisible(true);
    }
    
    private void initializeFrame() {
        setTitle("Đăng nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(CONTENT_BG);
    }
    
    private void createComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(CONTENT_BG);
        mainPanel.setBorder(new EmptyBorder(40, 50, 40, 50));
        
        // Logo/Title section
        JPanel logoPanel = createLogoPanel();
        mainPanel.add(logoPanel);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Login form card - căn giữa
        JPanel formCard = createFormCard();
        formCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(formCard);
        
        add(mainPanel);
        
        // Focus vô usernam
        SwingUtilities.invokeLater(() -> txtUsername.requestFocusInWindow());
    }
    
    private JPanel createLogoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CONTENT_BG);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Title
        JLabel titleLabel = new JLabel("KHO HÀNG ĐIỆN THOẠI");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        
        panel.add(Box.createVerticalStrut(5));
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Đăng nhập để tiếp tục");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(subtitleLabel);
        
        return panel;
    }
    
    private JPanel createFormCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(30, 30, 30, 30));
        card.setPreferredSize(new Dimension(340, 300));
        card.setMaximumSize(new Dimension(340, 300));
        card.setMinimumSize(new Dimension(340, 300));
        
        // Username field
        card.add(createFormGroup("Tên đăng nhập", txtUsername = createTextField("Nhập tên đăng nhập...")));
        card.add(Box.createVerticalStrut(18));
        
        // Password field
        card.add(createFormGroup("Mật khẩu", txtPassword = createPasswordField("Nhập mật khẩu...")));
        card.add(Box.createVerticalStrut(8));
        
        // Error label
        lblError = new JLabel(" ");
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblError.setForeground(ERROR_COLOR);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblError);
        card.add(Box.createVerticalStrut(12));
        
        // Login button
        btnLogin = createLoginButton();
        card.add(btnLogin);
        
        // Enter key listener
        KeyListener enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        };
        txtUsername.addKeyListener(enterKeyListener);
        txtPassword.addKeyListener(enterKeyListener);
        
        return card;
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
                    g2.setColor(TEXT_LIGHT);
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(placeholder, getInsets().left, 
                        g.getFontMetrics().getMaxAscent() + getInsets().top + 2);
                    g2.dispose();
                }
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(280, 45));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(new CompoundBorder(
                    new LineBorder(PRIMARY_COLOR, 2, true),
                    new EmptyBorder(9, 14, 9, 14)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(new CompoundBorder(
                    new LineBorder(BORDER_COLOR, 1, true),
                    new EmptyBorder(10, 15, 10, 15)
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
                    g2.setColor(TEXT_LIGHT);
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(placeholder, getInsets().left, 
                        g.getFontMetrics().getMaxAscent() + getInsets().top + 2);
                    g2.dispose();
                }
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(280, 45));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(new CompoundBorder(
                    new LineBorder(PRIMARY_COLOR, 2, true),
                    new EmptyBorder(9, 14, 9, 14)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(new CompoundBorder(
                    new LineBorder(BORDER_COLOR, 1, true),
                    new EmptyBorder(10, 15, 10, 15)
                ));
            }
        });
        
        return field;
    }
    
    private JButton createLoginButton() {
        JButton button = new JButton("Đăng nhập");
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setPreferredSize(new Dimension(280, 48));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY_HOVER);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        
        button.addActionListener(e -> performLogin());
        
        return button;
    }
    
    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        // Validate input
        if (username.isEmpty()) {
            showError("Vui lòng nhập tên đăng nhập");
            txtUsername.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            showError("Vui lòng nhập mật khẩu");
            txtPassword.requestFocus();
            return;
        }
        
        // Disable button while processing
        btnLogin.setEnabled(false);
        btnLogin.setText("Đang đăng nhập...");
        
        // Perform login in background
        SwingWorker<AccountDTO, Void> worker = new SwingWorker<>() {
            @Override
            protected AccountDTO doInBackground() {
                AccountDAO accountDAO = new AccountDAO();
                return accountDAO.Login(username, password);
            }
            
            @Override
            protected void done() {
                try {
                    AccountDTO account = get();
                    if (account != null) {
                        // Login successful
                        SessionManager.getInstance().setCurrentUser(account);
                        LogHelper.logLogin(account.getID());
                        dispose();
                        new MainFrame();
                    } else {
                        showError("Tên đăng nhập hoặc mật khẩu không đúng");
                        txtPassword.setText("");
                        txtPassword.requestFocus();
                    }
                } catch (Exception e) {
                    showError("Lỗi kết nối cơ sở dữ liệu");
                    e.printStackTrace();
                } finally {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Đăng nhập");
                }
            }
        };
        worker.execute();
    }
    
    private void showError(String message) {
        lblError.setText(message);
        
        // Shake animation
        Timer timer = new Timer(50, null);
        final int[] count = {0};
        final int originalX = getLocation().x;
        
        timer.addActionListener(e -> {
            count[0]++;
            if (count[0] <= 6) {
                int offset = (count[0] % 2 == 0) ? 5 : -5;
                setLocation(originalX + offset, getLocation().y);
            } else {
                setLocation(originalX, getLocation().y);
                timer.stop();
            }
        });
        timer.start();
    }
    
    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}
