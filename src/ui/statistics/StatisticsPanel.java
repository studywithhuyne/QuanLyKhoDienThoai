package ui.statistics;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import static ui.UIColor.*;

/**
 * Panel thống kê - Thống kê nhập hàng, xuất hàng, tồn kho
 */
public class StatisticsPanel extends JPanel {
    
    private JFrame parentFrame;
    
    public StatisticsPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
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
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(CONTENT_BG);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        statsPanel.add(createStatCard("📥", "Tổng nhập tháng này", "1,500,000,000₫", "15 phiếu", PRIMARY_COLOR));
        statsPanel.add(createStatCard("📤", "Tổng xuất tháng này", "892,490,000₫", "45 hóa đơn", SUCCESS_COLOR));
        statsPanel.add(createStatCard("📦", "Giá trị tồn kho", "2,450,000,000₫", "248 SKU", WARNING_COLOR));
        
        contentWrapper.add(statsPanel);
        contentWrapper.add(Box.createVerticalStrut(25));
        
        // Charts row
        JPanel chartsRow = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsRow.setBackground(CONTENT_BG);
        
        // Nhập hàng chart
        JPanel importChart = createCard("Thống kê nhập hàng");
        JPanel importContent = new JPanel(new BorderLayout());
        importContent.setBackground(CARD_BG);
        importContent.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel importPlaceholder = new JLabel("📊 Biểu đồ nhập hàng theo tháng", SwingConstants.CENTER);
        importPlaceholder.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        importPlaceholder.setForeground(TEXT_SECONDARY);
        importContent.add(importPlaceholder, BorderLayout.CENTER);
        importChart.add(importContent, BorderLayout.CENTER);
        
        chartsRow.add(importChart);
        
        // Xuất hàng chart
        JPanel exportChart = createCard("Thống kê xuất hàng");
        JPanel exportContent = new JPanel(new BorderLayout());
        exportContent.setBackground(CARD_BG);
        exportContent.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel exportPlaceholder = new JLabel("📈 Biểu đồ xuất hàng theo tháng", SwingConstants.CENTER);
        exportPlaceholder.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        exportPlaceholder.setForeground(TEXT_SECONDARY);
        exportContent.add(exportPlaceholder, BorderLayout.CENTER);
        exportChart.add(exportContent, BorderLayout.CENTER);
        
        chartsRow.add(exportChart);
        
        contentWrapper.add(chartsRow);
        contentWrapper.add(Box.createVerticalStrut(25));
        
        // Inventory chart
        JPanel inventoryChart = createCard("Thống kê tồn kho theo danh mục");
        JPanel inventoryContent = new JPanel(new BorderLayout());
        inventoryContent.setBackground(CARD_BG);
        inventoryContent.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel inventoryPlaceholder = new JLabel("📦 Biểu đồ tồn kho theo danh mục", SwingConstants.CENTER);
        inventoryPlaceholder.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        inventoryPlaceholder.setForeground(TEXT_SECONDARY);
        inventoryContent.add(inventoryPlaceholder, BorderLayout.CENTER);
        inventoryChart.add(inventoryContent, BorderLayout.CENTER);
        
        contentWrapper.add(inventoryChart);
        
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
}
