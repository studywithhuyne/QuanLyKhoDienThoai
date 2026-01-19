package ui.dashboard;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import static ui.UIColor.*;

/**
 * Panel hiển thị Dashboard - Trang chủ
 */
public class DashboardPanel extends JPanel {
    
    public DashboardPanel() {
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(CONTENT_BG);
        setBorder(new EmptyBorder(25, 30, 25, 30));
        
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
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createStatCard(String icon, String title, String value, String subtitle, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR, 1, true), new EmptyBorder(20, 20, 20, 20)));
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        
        // Icon and title row
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topRow.setOpaque(false);
        
        JPanel iconPanel = new JPanel(new GridBagLayout());
        iconPanel.setPreferredSize(new Dimension(40, 40));
        iconPanel.setBackground(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30));
        iconPanel.setOpaque(true);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        iconPanel.add(iconLabel);
        
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLbl.setForeground(TEXT_SECONDARY);
        
        topRow.add(iconPanel);
        topRow.add(titleLbl);
        
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
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR), new EmptyBorder(15, 20, 15, 20)));
        
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLbl.setForeground(TEXT_PRIMARY);
        header.add(titleLbl, BorderLayout.WEST);
        
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
        
        JPanel iconPanel = new JPanel(new GridBagLayout());
        iconPanel.setPreferredSize(new Dimension(32, 32));
        iconPanel.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        iconPanel.setOpaque(true);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        iconPanel.add(iconLabel);
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textLabel.setForeground(TEXT_PRIMARY);
        
        leftSection.add(iconPanel);
        leftSection.add(textLabel);
        
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(TEXT_SECONDARY);
        
        item.add(leftSection, BorderLayout.WEST);
        item.add(timeLabel, BorderLayout.EAST);
        
        return item;
    }
}
