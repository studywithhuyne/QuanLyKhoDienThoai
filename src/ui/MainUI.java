package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class MainUI extends JFrame {
    
    // Colors - Modern Dark Theme
    private static final Color PRIMARY_COLOR = new Color(45, 55, 72);      // Brighter Indigo
    //Màu xanh đậm (99, 102, 241)
    private static final Color DARK_BLUE = new Color(99, 102, 241);
    private static final Color PRIMARY_HOVER = new Color(129, 140, 248);     // Lighter Indigo
    private static final Color SIDEBAR_BG = new Color(17, 24, 39);           // Dark Gray
    private static final Color SIDEBAR_HOVER = new Color(45, 55, 72);        // Slightly lighter
    private static final Color CONTENT_BG = new Color(243, 244, 246);        // Light Gray
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(17, 24, 39);
    private static final Color TEXT_SECONDARY = new Color(156, 163, 175);    // Brighter gray
    private static final Color TEXT_LIGHT = new Color(255, 255, 255);        // Pure white
    private static final Color TEXT_MENU = new Color(209, 213, 219);         // Light gray for menu
    private static final Color ICON_COLOR = new Color(167, 139, 250);        // Purple for icons
    private static final Color ICON_DASHBOARD = new Color(96, 165, 250);     // Blue
    private static final Color ICON_PRODUCT = new Color(52, 211, 153);       // Green
    private static final Color ICON_IMPORT = new Color(251, 191, 36);        // Yellow
    private static final Color ICON_SALES = new Color(251, 113, 133);        // Pink
    private static final Color ICON_SUPPLIER = new Color(129, 140, 248);     // Indigo
    private static final Color ICON_BRAND = new Color(244, 114, 182);        // Pink
    private static final Color ICON_CATEGORY = new Color(56, 189, 248);      // Cyan
    private static final Color ICON_ACCOUNT = new Color(163, 230, 53);       // Lime
    private static final Color BORDER_COLOR = new Color(229, 231, 235);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color WARNING_COLOR = new Color(251, 191, 36);      // Brighter yellow
    private static final Color DANGER_COLOR = new Color(248, 113, 113);      // Brighter red
    private static final Color GREEN = new Color(22, 186, 44);      // Green
    
    // Components
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private JPanel headerPanel;
    private JLabel titleLabel;
    private JButton currentActiveButton;
    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    
    // Menu items
    private String[] menuItems = {
        "Dashboard", "Sản phẩm", "Nhập kho", "Bán hàng", 
        "Nhà cung cấp", "Thương hiệu", "Danh mục", "Tài khoản"
    };
    
    private String[] menuIcons = {
        "📊", "📱", "📥", "🛒", "🏭", "🏷️", "📂", "👤"
    };
    
    private Color[] menuIconColors = {
        new Color(96, 165, 250),   // Dashboard - Blue
        new Color(52, 211, 153),   // Sản phẩm - Green  
        new Color(251, 191, 36),   // Nhập kho - Yellow
        new Color(251, 113, 133),  // Bán hàng - Pink
        new Color(129, 140, 248),  // Nhà cung cấp - Indigo
        new Color(244, 114, 182),  // Thương hiệu - Pink
        new Color(56, 189, 248),   // Danh mục - Cyan
        new Color(163, 230, 53)    // Tài khoản - Lime
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
        
        // Set custom icon
        try {
            // You can set a custom icon here
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        
        // Add panels for each menu item
        mainContentPanel.add(createDashboardPanel(), "Dashboard");
        mainContentPanel.add(createProductPanel(), "Sản phẩm");
        mainContentPanel.add(createImportPanel(), "Nhập kho");
        mainContentPanel.add(createSalesPanel(), "Bán hàng");
        mainContentPanel.add(createSupplierPanel(), "Nhà cung cấp");
        mainContentPanel.add(createBrandPanel(), "Thương hiệu");
        mainContentPanel.add(createCategoryPanel(), "Danh mục");
        mainContentPanel.add(createAccountPanel(), "Tài khoản");
        
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
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 25));
        logoPanel.setBackground(SIDEBAR_BG);
        logoPanel.setMaximumSize(new Dimension(260, 80));
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel logoIcon = new JLabel("📦");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        
        JLabel logoText = new JLabel("PhoneStock");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logoText.setForeground(Color.WHITE);
        
        logoPanel.add(logoIcon);
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
        menuLabel.setForeground(new Color(148, 163, 184));  // Brighter label
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
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed() || this == currentActiveButton) {
                    g2.setColor(PRIMARY_COLOR);
                    g2.fill(new RoundRectangle2D.Float(15, 0, getWidth() - 30, getHeight(), 12, 12));
                } else if (getModel().isRollover()) {
                    g2.setColor(SIDEBAR_HOVER);
                    g2.fill(new RoundRectangle2D.Float(15, 0, getWidth() - 30, getHeight(), 12, 12));
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        button.setMaximumSize(new Dimension(260, 48));
        button.setPreferredSize(new Dimension(260, 48));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBackground(SIDEBAR_BG);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Icon with colored background circle
        JLabel iconLabel = new JLabel(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Draw colored background circle
                g2.setColor(new Color(iconColor.getRed(), iconColor.getGreen(), iconColor.getBlue(), 40));
                g2.fillRoundRect(0, 0, 32, 32, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        iconLabel.setPreferredSize(new Dimension(32, 32));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textLabel.setForeground(new Color(241, 245, 249));  // Very bright white
        
        button.add(iconLabel);
        button.add(textLabel);
        
        button.addActionListener(e -> {
            setActiveButton(button);
            cardLayout.show(mainContentPanel, text);
            titleLabel.setText(text);
        });
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.repaint();
            }
        });
        
        return button;
    }
    
    private void setActiveButton(JButton button) {
        if (currentActiveButton != null) {
            currentActiveButton.repaint();
        }
        currentActiveButton = button;
        button.repaint();
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
        JLabel avatar = new JLabel("👤") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PRIMARY_COLOR);
                g2.fillOval(0, 0, 40, 40);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setPreferredSize(new Dimension(40, 40));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(SIDEBAR_BG);
        
        JLabel nameLabel = new JLabel("Admin");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(Color.WHITE);
        
        JLabel roleLabel = new JLabel("Quản trị viên");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLabel.setForeground(new Color(148, 163, 184));  // Brighter secondary text
        
        textPanel.add(nameLabel);
        textPanel.add(roleLabel);
        
        infoPanel.add(avatar);
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
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(15, 30, 15, 30)
        ));
        
        // Title
        titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        header.add(titleLabel, BorderLayout.WEST);
        
        // Right section - Search and notifications
        JPanel rightSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightSection.setBackground(CARD_BG);
        
        // Search field
        JTextField searchField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(TEXT_SECONDARY);
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    g2.drawString("Tìm kiếm...", 15, 25);
                    g2.dispose();
                }
            }
        };
        searchField.setPreferredSize(new Dimension(250, 40));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(5, 15, 5, 15)
        ));
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
        JButton button = new JButton(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isRollover()) {
                    g2.setColor(CONTENT_BG);
                    g2.fillOval(0, 0, getWidth(), getHeight());
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(40, 40));
        button.setBackground(CARD_BG);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);
        return button;
    }
    
    // ==================== DASHBOARD PANEL ====================
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CONTENT_BG);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));
        
        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setBackground(CONTENT_BG);
        
        // Stats cards row
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBackground(CONTENT_BG);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        statsPanel.add(createStatCard("📱", "Sản phẩm", "10", "+2 mới", SUCCESS_COLOR));
        statsPanel.add(createStatCard("📦", "Tồn kho", "248", "SKU items", PRIMARY_COLOR));
        statsPanel.add(createStatCard("📥", "Nhập hôm nay", "15", "Phiếu nhập", WARNING_COLOR));
        statsPanel.add(createStatCard("🛒", "Bán hôm nay", "52,490,000₫", "+12%", SUCCESS_COLOR));
        
        contentWrapper.add(statsPanel);
        contentWrapper.add(Box.createVerticalStrut(25));
        
        // Charts and recent activities
        JPanel chartsRow = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsRow.setBackground(CONTENT_BG);
        
        // Chart placeholder
        JPanel chartCard = createCard("Doanh thu tuần này");
        JPanel chartContent = new JPanel(new BorderLayout());
        chartContent.setBackground(CARD_BG);
        chartContent.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel chartPlaceholder = new JLabel("📈 Biểu đồ doanh thu", SwingConstants.CENTER);
        chartPlaceholder.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        chartPlaceholder.setForeground(TEXT_SECONDARY);
        chartContent.add(chartPlaceholder, BorderLayout.CENTER);
        chartCard.add(chartContent, BorderLayout.CENTER);
        
        chartsRow.add(chartCard);
        
        // Recent activities
        JPanel activitiesCard = createCard("Hoạt động gần đây");
        JPanel activitiesContent = new JPanel();
        activitiesContent.setLayout(new BoxLayout(activitiesContent, BoxLayout.Y_AXIS));
        activitiesContent.setBackground(CARD_BG);
        activitiesContent.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        activitiesContent.add(createActivityItem("🛒", "Bán iPhone 17 Pro Max", "10:30 AM", SUCCESS_COLOR));
        activitiesContent.add(createActivityItem("📥", "Nhập hàng từ FPT Synnex", "09:15 AM", PRIMARY_COLOR));
        activitiesContent.add(createActivityItem("👤", "Đăng nhập: jerry", "08:00 AM", WARNING_COLOR));
        activitiesContent.add(createActivityItem("📦", "Cập nhật tồn kho", "Hôm qua", TEXT_SECONDARY));
        
        activitiesCard.add(activitiesContent, BorderLayout.CENTER);
        chartsRow.add(activitiesCard);
        
        contentWrapper.add(chartsRow);
        
        JScrollPane scrollPane = new JScrollPane(contentWrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(CONTENT_BG);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatCard(String icon, String title, String value, String subtitle, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        
        // Icon and title row
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topRow.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30));
                g2.fillOval(0, 0, 40, 40);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        iconLabel.setPreferredSize(new Dimension(40, 40));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(TEXT_SECONDARY);
        
        topRow.add(iconLabel);
        topRow.add(titleLabel);
        
        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(TEXT_PRIMARY);
        valueLabel.setBorder(new EmptyBorder(10, 5, 5, 0));
        
        // Subtitle
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(accentColor);
        subtitleLabel.setBorder(new EmptyBorder(0, 5, 0, 0));
        
        content.add(topRow);
        content.add(valueLabel);
        content.add(subtitleLabel);
        
        card.add(content, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createCard(String title) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);
        header.add(titleLabel, BorderLayout.WEST);
        
        card.add(header, BorderLayout.NORTH);
        
        return card;
    }
    
    private JPanel createActivityItem(String icon, String text, String time, Color color) {
        JPanel item = new JPanel(new BorderLayout());
        item.setOpaque(false);
        item.setBorder(new EmptyBorder(12, 0, 12, 0));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JPanel leftSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftSection.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                g2.fillOval(0, 0, 32, 32);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconLabel.setPreferredSize(new Dimension(32, 32));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textLabel.setForeground(TEXT_PRIMARY);
        
        leftSection.add(iconLabel);
        leftSection.add(textLabel);
        
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(TEXT_SECONDARY);
        
        item.add(leftSection, BorderLayout.WEST);
        item.add(timeLabel, BorderLayout.EAST);
        
        return item;
    }
    
    // ==================== PRODUCT PANEL ====================
    private JTable productTable;
    private Object[][] productData = {
        {1, "iPhone 17 Pro Max", "Apple", "Điện thoại", 2, 13},
        {2, "Samsung Galaxy S26 Ultra", "Samsung", "Điện thoại", 2, 15},
        {3, "Xiaomi 15 Ultra", "Xiaomi", "Điện thoại", 0, 0},
        {4, "Anker PowerLine III Flow USB-C", "Anker", "Cáp sạc", 1, 50},
        {5, "Baseus Crystal Shine Cable", "Baseus", "Cáp sạc", 1, 60},
    };
    private String[] productColumns = {"ID", "Tên sản phẩm", "Thương hiệu", "Danh mục", "SKU", "Tồn kho"};
    
    private JPanel createProductPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CONTENT_BG);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));
        
        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionPanel.setBackground(CONTENT_BG);
        actionPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JButton addBtn = createActionButton("➕ Thêm mới", DARK_BLUE);
        JButton editBtn = createActionButton("✏️ Sửa", WARNING_COLOR);
        JButton deleteBtn = createActionButton("🗑️ Xóa", DANGER_COLOR);
        JButton refreshBtn = createActionButton("🔄 Làm mới", GREEN);
        addBtn.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        refreshBtn.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        editBtn.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        deleteBtn.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        
        // Add button action - Open ProductAddUI
        addBtn.addActionListener(e -> {
            new ProductAddUI(MainUI.this);
            // TODO: Refresh table after adding
        });
        
        // Edit button action - Open ProductEditUI
        editBtn.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn sản phẩm cần sửa!", 
                    "Thông báo", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int id = (int) productTable.getValueAt(selectedRow, 0);
            String name = (String) productTable.getValueAt(selectedRow, 1);
            String brand = (String) productTable.getValueAt(selectedRow, 2);
            String category = (String) productTable.getValueAt(selectedRow, 3);
            
            new ProductEditUI(MainUI.this, id, name, brand, category);
            // TODO: Refresh table after editing
        });
        
        // Delete button action - Open ProductDeleteUI
        deleteBtn.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn sản phẩm cần xóa!", 
                    "Thông báo", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int id = (int) productTable.getValueAt(selectedRow, 0);
            String name = (String) productTable.getValueAt(selectedRow, 1);
            
            new ProductDeleteUI(MainUI.this, id, name);
            // TODO: Refresh table after deleting
        });
        
        // Refresh button action
        refreshBtn.addActionListener(e -> {
            // TODO: Reload data from database
            JOptionPane.showMessageDialog(this, 
                "Đã làm mới dữ liệu!", 
                "Thông báo", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        actionPanel.add(addBtn);
        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);
        actionPanel.add(refreshBtn);
        
        panel.add(actionPanel, BorderLayout.NORTH);
        
        // Table
        productTable = new JTable(productData, productColumns);
        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        productTable.setRowHeight(45);
        productTable.setShowGrid(false);
        productTable.setIntercellSpacing(new Dimension(0, 0));
        productTable.setBackground(CARD_BG);
        productTable.setSelectionBackground(new Color(79, 70, 229, 30));
        productTable.setSelectionForeground(TEXT_PRIMARY);
        productTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        
        // Context menu (Right-click menu)
        JPopupMenu contextMenu = new JPopupMenu();
        contextMenu.setBackground(CARD_BG);
        contextMenu.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        
        JMenuItem menuAdd = createMenuItem("➕ Thêm mới", DARK_BLUE);
        JMenuItem menuEdit = createMenuItem("✏️ Sửa", WARNING_COLOR);
        JMenuItem menuDelete = createMenuItem("🗑️ Xóa", DANGER_COLOR);
        menuAdd.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        menuEdit.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        menuDelete.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        
        menuAdd.addActionListener(e -> {
            new ProductAddUI(MainUI.this);
        });
        
        menuEdit.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn sản phẩm cần sửa!", 
                    "Thông báo", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) productTable.getValueAt(selectedRow, 0);
            String name = (String) productTable.getValueAt(selectedRow, 1);
            String brand = (String) productTable.getValueAt(selectedRow, 2);
            String category = (String) productTable.getValueAt(selectedRow, 3);
            new ProductEditUI(MainUI.this, id, name, brand, category);
        });
        
        menuDelete.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn sản phẩm cần xóa!", 
                    "Thông báo", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) productTable.getValueAt(selectedRow, 0);
            String name = (String) productTable.getValueAt(selectedRow, 1);
            new ProductDeleteUI(MainUI.this, id, name);
        });
        
        contextMenu.add(menuAdd);
        contextMenu.addSeparator();
        contextMenu.add(menuEdit);
        contextMenu.add(menuDelete);
        
        // Add mouse listener for right-click
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }
            
            private void showContextMenu(MouseEvent e) {
                int row = productTable.rowAtPoint(e.getPoint());
                if (row >= 0 && row < productTable.getRowCount()) {
                    productTable.setRowSelectionInterval(row, row);
                }
                contextMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        
        // Header style
        productTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        productTable.getTableHeader().setBackground(CARD_BG);
        productTable.getTableHeader().setForeground(TEXT_PRIMARY);
        productTable.getTableHeader().setPreferredSize(new Dimension(0, 50));
        productTable.getTableHeader().setBorder(new MatteBorder(0, 0, 2, 0, BORDER_COLOR));
        
        // Cell renderer for alternating row colors
        productTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? CARD_BG : new Color(249, 250, 251));
                }
                setBorder(new EmptyBorder(0, 15, 0, 15));
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(productTable) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scrollPane.getViewport().setBackground(CARD_BG);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createImportPanel() {
        return createDataPanel("Phiếu nhập", 
            new String[]{"ID", "Nhà cung cấp", "Nhân viên", "Tổng tiền", "Ngày tạo"},
            new Object[][]{
                {1, "FPT Synnex", "Jerry", "1,500,000,000₫", "02/01/2026"},
            });
    }
    
    private JPanel createSalesPanel() {
        return createDataPanel("Hóa đơn", 
            new String[]{"ID", "Nhân viên", "Tổng tiền", "Ngày tạo"},
            new Object[][]{
                {1, "Jerry", "56,490,000₫", "05/01/2026"},
            });
    }
    
    private JPanel createSupplierPanel() {
        return createDataPanel("Nhà cung cấp", 
            new String[]{"ID", "Tên nhà cung cấp"},
            new Object[][]{
                {1, "FPT Synnex"},
                {2, "Viettel Store"},
                {3, "CellphoneS B2B"},
                {4, "Anker Vietnam"},
                {5, "Baseus Official"},
                {6, "Ugreen Vietnam"},
            });
    }
    
    private JPanel createBrandPanel() {
        return createDataPanel("Thương hiệu", 
            new String[]{"ID", "Tên thương hiệu"},
            new Object[][]{
                {1, "Apple"}, {2, "Samsung"}, {3, "Xiaomi"}, {4, "Oppo"},
                {5, "Anker"}, {6, "Baseus"}, {7, "Belkin"}, {8, "Sony"}, {9, "Ugreen"},
            });
    }
    
    private JPanel createCategoryPanel() {
        return createDataPanel("Danh mục", 
            new String[]{"ID", "Tên danh mục"},
            new Object[][]{
                {1, "Điện thoại"}, {2, "Cáp sạc"}, {3, "Cường lực"},
                {4, "Sạc dự phòng"}, {5, "Củ sạc"}, {6, "Loa"},
            });
    }
    
    private JPanel createAccountPanel() {
        return createDataPanel("Tài khoản", 
            new String[]{"ID", "Username", "Họ tên", "Vai trò", "Ngày tạo"},
            new Object[][]{
                {1, "admin", "Quản lý", "Admin", "01/01/2026"},
                {2, "jerry", "Jerry", "Staff", "01/01/2026"},
            });
    }
    
    private JPanel createDataPanel(String title, String[] columns, Object[][] data) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CONTENT_BG);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));
        
        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionPanel.setBackground(CONTENT_BG);
        actionPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JButton addBtn = createActionButton("➕ Thêm mới", DARK_BLUE);
        JButton editBtn = createActionButton("✏️ Sửa", WARNING_COLOR);
        JButton deleteBtn = createActionButton("🗑️ Xóa", DANGER_COLOR);
        JButton refreshBtn = createActionButton("🔄 Làm mới", GREEN);
        addBtn.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        refreshBtn.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        editBtn.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        deleteBtn.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        
        actionPanel.add(addBtn);
        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);
        actionPanel.add(refreshBtn);
        
        panel.add(actionPanel, BorderLayout.NORTH);
        
        // Table
        JTable table = new JTable(data, columns);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(45);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(CARD_BG);
        table.setSelectionBackground(new Color(79, 70, 229, 30));
        table.setSelectionForeground(TEXT_PRIMARY);
        
        // Header style
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(CARD_BG);
        table.getTableHeader().setForeground(TEXT_PRIMARY);
        table.getTableHeader().setPreferredSize(new Dimension(0, 50));
        table.getTableHeader().setBorder(new MatteBorder(0, 0, 2, 0, BORDER_COLOR));
        
        // Cell renderer for alternating row colors
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? CARD_BG : new Color(249, 250, 251));
                }
                setBorder(new EmptyBorder(0, 15, 0, 15));
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scrollPane.getViewport().setBackground(CARD_BG);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(color);
                } else {
                    g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
                }
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(color.equals(PRIMARY_COLOR) || getModel(button) ? Color.WHITE : color);
        button.setPreferredSize(new Dimension(120, 38));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(color);
            }
        });
        
        return button;
    }
    
    private boolean getModel(JButton button) {
        return button.getModel().isRollover() || button.getModel().isPressed();
    }
    
    private JMenuItem createMenuItem(String text, Color color) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        menuItem.setForeground(TEXT_PRIMARY);
        menuItem.setBackground(CARD_BG);
        menuItem.setBorder(new EmptyBorder(8, 15, 8, 15));
        menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menuItem.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                menuItem.setForeground(color);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                menuItem.setBackground(CARD_BG);
                menuItem.setForeground(TEXT_PRIMARY);
            }
        });
        
        return menuItem;
    }
    
    public static void main(String[] args) {
        // Set system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Anti-aliasing
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new MainUI());
    }
}
