package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import static ui.UIColor.*;

public class MainUI extends JFrame {
    
    // Components
    private JPanel sidebarPanel;
    private JPanel headerPanel;
    private JLabel titleLabel;
    private JButton currentActiveButton;
    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    
    // Menu items
    private String[] menuItems = {"Dashboard", "Sản phẩm", "Nhập kho", "Bán hàng", "Nhà cung cấp", "Thương hiệu", "Danh mục", "Tài khoản"};
    private String[] menuIcons = {"📊", "📱", "📥", "🛒", "🏭", "🏷️", "📂", "👤"};
    private Color[] menuIconColors = {
        new Color(64, 156, 255),   // Dashboard - Vivid Blue
        new Color(46, 213, 115),   // Sản phẩm - Vivid Green
        new Color(255, 159, 67),   // Nhập kho - Vivid Orange
        new Color(255, 71, 87),    // Bán hàng - Vivid Red
        new Color(83, 82, 237),    // Nhà cung cấp - Vivid Indigo
        new Color(255, 107, 129),  // Thương hiệu - Vivid Pink
        new Color(112, 161, 255),  // Danh mục - Vivid Light Blue
        new Color(236, 204, 104)   // Tài khoản - Vivid Yellow
    };
    
    public MainUI() {
        initializeFrame();
        createComponents();
        setVisible(true);
    }
    
    private void initializeFrame() {
        setTitle("Hệ thống Quản lý Kho Điện Thoại");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 850);
        setMinimumSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }
    
    private void createComponents() {
        // Create sidebar
        sidebarPanel = createSidebar();
        add(sidebarPanel, BorderLayout.WEST);
        
        // Create main content area
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(CONTENT_BG);
        
        // Create header
        headerPanel = createHeader();
        rightPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create content panel with CardLayout
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(CONTENT_BG);
        
        // Add panels for each menu item - Sử dụng các class panel riêng biệt
        mainContentPanel.add(new DashboardPanel(), "Dashboard");
        mainContentPanel.add(new ProductPanel(this), "Sản phẩm");
        mainContentPanel.add(new ImportPanel(this), "Nhập kho");
        mainContentPanel.add(new SalesPanel(this), "Bán hàng");
        mainContentPanel.add(new SupplierPanel(this), "Nhà cung cấp");
        mainContentPanel.add(new BrandPanel(this), "Thương hiệu");
        mainContentPanel.add(new CategoryPanel(this), "Danh mục");
        mainContentPanel.add(new AccountPanel(this), "Tài khoản");
        
        rightPanel.add(mainContentPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.CENTER);
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(260, getHeight()));
        sidebar.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        // Logo section
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 25));
        logoPanel.setBackground(SIDEBAR_BG);
        logoPanel.setMaximumSize(new Dimension(260, 80));
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel logoText = new JLabel("KHO HÀNG ĐIỆN THOẠI");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoText.setForeground(Color.WHITE);
        
        logoPanel.add(logoText);
        sidebar.add(logoPanel);
        
        // Separator
        sidebar.add(Box.createVerticalStrut(10));
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(55, 65, 81));
        separator.setMaximumSize(new Dimension(220, 1));
        sidebar.add(separator);
        sidebar.add(Box.createVerticalStrut(20));
        
        // Menu section label
        JPanel menuLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        menuLabelPanel.setBackground(SIDEBAR_BG);
        menuLabelPanel.setMaximumSize(new Dimension(260, 30));
        menuLabelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel menuLabel = new JLabel("MENU");
        menuLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        menuLabel.setForeground(new Color(148, 163, 184));
        menuLabelPanel.add(menuLabel);
        sidebar.add(menuLabelPanel);
        sidebar.add(Box.createVerticalStrut(10));
        
        // Menu items
        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createMenuButton(menuIcons[i], menuItems[i], menuIconColors[i]);
            sidebar.add(menuButton);
            sidebar.add(Box.createVerticalStrut(5));
            
            // Set first button as active
            if (i == 0) {
                setActiveButton(menuButton);
            }
        }
        
        // Spacer
        sidebar.add(Box.createVerticalGlue());
        
        // User section
        sidebar.add(createUserSection());
        
        return sidebar;
    }
    
    private JButton createMenuButton(String icon, String text, Color iconColor) {
        JButton button = new JButton();
        button.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        button.setMaximumSize(new Dimension(260, 48));
        button.setPreferredSize(new Dimension(260, 48));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBackground(SIDEBAR_BG);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        // Icon with colored background
        JPanel iconPanel = new JPanel(new GridBagLayout());
        iconPanel.setPreferredSize(new Dimension(32, 32));
        iconPanel.setOpaque(true);
        iconPanel.setBackground(new Color(iconColor.getRed(), iconColor.getGreen(), iconColor.getBlue(), 40));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        iconPanel.add(iconLabel);
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textLabel.setForeground(new Color(241, 245, 249));
        
        button.add(iconPanel);
        button.add(textLabel);
        
        button.addActionListener(e -> {
            setActiveButton(button);
            cardLayout.show(mainContentPanel, text);
            titleLabel.setText(text);
        });
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != currentActiveButton) {
                    button.setBackground(SIDEBAR_HOVER);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (button != currentActiveButton) {
                    button.setBackground(SIDEBAR_BG);
                }
            }
        });
        
        return button;
    }
    
    private void setActiveButton(JButton button) {
        if (currentActiveButton != null) {
            currentActiveButton.setBackground(SIDEBAR_BG);
        }
        currentActiveButton = button;
        button.setBackground(PRIMARY_COLOR);
    }
    
    private JPanel createUserSection() {
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(SIDEBAR_BG);
        userPanel.setBorder(new EmptyBorder(15, 15, 20, 15));
        userPanel.setMaximumSize(new Dimension(260, 100));
        userPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(55, 65, 81));
        separator.setMaximumSize(new Dimension(230, 1));
        userPanel.add(separator);
        userPanel.add(Box.createVerticalStrut(15));
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        infoPanel.setBackground(SIDEBAR_BG);
        
        // Avatar
        JPanel avatarPanel = new JPanel(new GridBagLayout());
        avatarPanel.setPreferredSize(new Dimension(40, 40));
        avatarPanel.setBackground(PRIMARY_COLOR);
        avatarPanel.setOpaque(true);
        
        JLabel avatarIcon = new JLabel("👤");
        avatarIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        avatarPanel.add(avatarIcon);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(SIDEBAR_BG);
        
        JLabel nameLabel = new JLabel("Admin");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(Color.WHITE);
        
        JLabel roleLabel = new JLabel("Quản trị viên");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLabel.setForeground(new Color(148, 163, 184));
        
        textPanel.add(nameLabel);
        textPanel.add(roleLabel);
        
        infoPanel.add(avatarPanel);
        infoPanel.add(textPanel);
        
        // Logout button
        JButton logoutBtn = new JButton("🚪");
        logoutBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        logoutBtn.setBackground(SIDEBAR_BG);
        logoutBtn.setForeground(TEXT_LIGHT);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setToolTipText("Đăng xuất");
        infoPanel.add(logoutBtn);
        
        userPanel.add(infoPanel);
        
        return userPanel;
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CARD_BG);
        header.setPreferredSize(new Dimension(getWidth(), 70));
        header.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR), new EmptyBorder(15, 30, 15, 30)));
        
        // Title
        titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        header.add(titleLabel, BorderLayout.WEST);
        
        // Right section - Search and notifications
        JPanel rightSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightSection.setBackground(CARD_BG);
        
        // Search field
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(250, 40));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR, 1, true), new EmptyBorder(5, 15, 5, 15)));
        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm...");
        rightSection.add(searchField);
        
        // Notification button
        JButton notifBtn = createIconButton("🔔", "Thông báo");
        rightSection.add(notifBtn);
        
        // Settings button
        JButton settingsBtn = createIconButton("⚙️", "Cài đặt");
        rightSection.add(settingsBtn);
        
        header.add(rightSection, BorderLayout.EAST);
        
        return header;
    }
    
    private JButton createIconButton(String icon, String tooltip) {
        JButton button = new JButton(icon);
        button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(40, 40));
        button.setBackground(CARD_BG);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);
        button.setOpaque(true);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(CONTENT_BG);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(CARD_BG);
            }
        });
        
        return button;
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new MainUI());
    }
}
