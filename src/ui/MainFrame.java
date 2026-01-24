package ui;

import javax.swing.*;
import javax.swing.border.*;

import static utils.ColorUtil.*;

import java.awt.*;
import java.awt.event.*;
import com.formdev.flatlaf.FlatLightLaf; 

import ui.dashboard.DashboardPanel;
import ui.product.ProductPanel;
import ui.import_.ImportPanel;
import ui.sales.SalesPanel;
import ui.supplier.SupplierPanel;
import ui.brand.BrandPanel;
import ui.category.CategoryPanel;
import ui.account.AccountPanel;
import ui.attribute.AttributePanel;
import ui.sku.SkuPanel;
import ui.imei.ImeiPanel;
import ui.logs.LogsPanel;
import ui.statistics.StatisticsPanel;

public class MainFrame extends JFrame {
    
    // Components
    private JPanel sidebarPanel;
    private JPanel headerPanel;
    private JLabel titleLabel;
    private JButton currentActiveButton;
    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    
    // Menu items - Đã bổ sung đầy đủ theo bảng phân công
    private String[] menuItems = {
        "Dashboard", "Sản phẩm", "Thuộc tính", "SKU", "IMEI",
        "Nhập kho", "Bán hàng", "Nhà cung cấp", 
        "Thương hiệu", "Danh mục", "Tài khoản", "Thống kê", "Logs"
    };
    private String[] menuIcons = {
        "●", "●", "●", "●", "●",
        "●", "●", "●", 
        "●", "●", "●", "●", "●"
    };
    private Color[] menuIconColors = {
        new Color(64, 156, 255),   // Dashboard - Vivid Blue
        new Color(46, 213, 115),   // Sản phẩm - Vivid Green
        new Color(156, 136, 255),  // Thuộc tính - Vivid Purple
        new Color(255, 193, 7),    // SKU - Vivid Amber
        new Color(0, 188, 212),    // IMEI - Vivid Cyan
        new Color(255, 159, 67),   // Nhập kho - Vivid Orange
        new Color(255, 71, 87),    // Bán hàng - Vivid Red
        new Color(83, 82, 237),    // Nhà cung cấp - Vivid Indigo
        new Color(255, 107, 129),  // Thương hiệu - Vivid Pink
        new Color(112, 161, 255),  // Danh mục - Vivid Light Blue
        new Color(236, 204, 104),  // Tài khoản - Vivid Yellow
        new Color(76, 175, 80),    // Thống kê - Vivid Green
        new Color(158, 158, 158)   // Logs - Gray
    };
    
    public MainFrame() {
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
        mainContentPanel.add(new AttributePanel(this), "Thuộc tính");
        mainContentPanel.add(new SkuPanel(this), "SKU");
        mainContentPanel.add(new ImeiPanel(this), "IMEI");
        mainContentPanel.add(new ImportPanel(this), "Nhập kho");
        mainContentPanel.add(new SalesPanel(this), "Bán hàng");
        mainContentPanel.add(new SupplierPanel(this), "Nhà cung cấp");
        mainContentPanel.add(new BrandPanel(this), "Thương hiệu");
        mainContentPanel.add(new CategoryPanel(this), "Danh mục");
        mainContentPanel.add(new AccountPanel(this), "Tài khoản");
        mainContentPanel.add(new StatisticsPanel(this), "Thống kê");
        mainContentPanel.add(new LogsPanel(this), "Logs");
        
        rightPanel.add(mainContentPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.CENTER);
    }
    
    private JPanel createSidebar() {
        // Main sidebar container
        JPanel sidebarContainer = new JPanel(new BorderLayout());
        sidebarContainer.setBackground(SIDEBAR_BG);
        sidebarContainer.setPreferredSize(new Dimension(260, getHeight()));
        
        // Logo section (fixed at top)
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 25));
        logoPanel.setBackground(SIDEBAR_BG);
        logoPanel.setPreferredSize(new Dimension(260, 80));
        
        JLabel logoText = new JLabel("KHO HÀNG ĐIỆN THOẠI");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoText.setForeground(Color.WHITE);
        logoPanel.add(logoText);
        
        sidebarContainer.add(logoPanel, BorderLayout.NORTH);
        
        // Scrollable menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(SIDEBAR_BG);
        
        // Separator
        menuPanel.add(Box.createVerticalStrut(10));
        JSeparator separator = new JSeparator();
        separator.setForeground(MENU_SEPARATOR);
        separator.setMaximumSize(new Dimension(220, 1));
        menuPanel.add(separator);
        menuPanel.add(Box.createVerticalStrut(20));
        
        // Menu section label
        JPanel menuLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        menuLabelPanel.setBackground(SIDEBAR_BG);
        menuLabelPanel.setMaximumSize(new Dimension(260, 30));
        menuLabelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel menuLabel = new JLabel("MENU");
        menuLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        menuLabel.setForeground(MENU_LABEL);
        menuLabelPanel.add(menuLabel);
        menuPanel.add(menuLabelPanel);
        menuPanel.add(Box.createVerticalStrut(10));
        
        // Menu items
        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createMenuButton(menuIcons[i], menuItems[i], menuIconColors[i]);
            menuPanel.add(menuButton);
            menuPanel.add(Box.createVerticalStrut(5));
            
            // Set first button as active
            if (i == 0) {
                setActiveButton(menuButton);
            }
        }
        
        // Add some padding at bottom of menu
        menuPanel.add(Box.createVerticalStrut(20));
        
        // Create scroll pane for menu
        JScrollPane menuScrollPane = new JScrollPane(menuPanel);
        menuScrollPane.setBorder(null);
        menuScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        menuScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        menuScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        menuScrollPane.setBackground(SIDEBAR_BG);
        menuScrollPane.getViewport().setBackground(SIDEBAR_BG);
        
        // Style scrollbar
        menuScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        menuScrollPane.getVerticalScrollBar().setBackground(SIDEBAR_BG);
        
        sidebarContainer.add(menuScrollPane, BorderLayout.CENTER);
        
        // User section (fixed at bottom)
        sidebarContainer.add(createUserSection(), BorderLayout.SOUTH);
        
        return sidebarContainer;
    }
    
    private JButton createMenuButton(String icon, String text, Color iconColor) {
        JButton button = new JButton();
        button.setLayout(new GridBagLayout());
        button.setMaximumSize(new Dimension(260, 48));
        button.setPreferredSize(new Dimension(260, 48));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBackground(SIDEBAR_BG);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;
        
        // Icon - vẽ hình tròn thực sự thay vì dùng ký tự text
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(iconColor);
                int size = 10;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                g2d.fillOval(x, y, size, size);
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(20, 20));
        
        // Text label  
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textLabel.setForeground(MENU_TEXT);
        
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 20, 0, 12);
        button.add(iconPanel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        button.add(textLabel, gbc);
        
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
        separator.setForeground(MENU_SEPARATOR);
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
        roleLabel.setForeground(MENU_LABEL);
        
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
        

        
        // Notification button
        JButton notifBtn = createIconButton("🔔", "Thông báo");
        rightSection.add(notifBtn);
        
        // Settings button
        JButton settingsBtn = createIconButton("⚙", "Cài đặt");
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
            FlatLightLaf.setup();
            
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
