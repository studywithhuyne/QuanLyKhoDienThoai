package ui.dashboard;

import javax.swing.*;
import javax.swing.border.*;

import static utils.ColorUtil.*;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

import bus.StatisticsBUS;
import bus.LogBUS;
import bus.ProductBUS;
import bus.SkuBUS;
import dto.LogDTO;
import java.util.List;
import java.text.SimpleDateFormat;

/**
 * Panel hiển thị Dashboard - Trang chủ
 */
public class DashboardPanel extends JPanel {
    
    private NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
    private JPanel contentWrapper;
    
    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(CONTENT_BG);
        setBorder(new EmptyBorder(25, 30, 25, 30));
        loadData();
    }
    
    /**
     * Load/Reload tất cả dữ liệu từ database
     */
    public void loadData() {
        // Xóa nội dung cũ
        removeAll();
        
        contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setBackground(CONTENT_BG);
        
        // Load real data
        StatisticsBUS statsBUS = new StatisticsBUS();
        ProductBUS productBUS = new ProductBUS();
        SkuBUS skuBUS = new SkuBUS();
        
        int productCount = productBUS.getAll().size();
        int totalStock = skuBUS.getAll().stream().mapToInt(s -> s.getStock()).sum();
        int todayImports = statsBUS.getTodayImportCount();
        double todayRevenue = statsBUS.getTodayRevenue();
        
        // Stats cards row
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBackground(CONTENT_BG);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        statsPanel.add(createStatCard("Sản phẩm", String.valueOf(productCount), "sản phẩm", SUCCESS_COLOR));
        statsPanel.add(createStatCard("Tồn kho", String.valueOf(totalStock), "sản phẩm", PRIMARY_COLOR));
        statsPanel.add(createStatCard("Nhập hôm nay", String.valueOf(todayImports), "Phiếu nhập", WARNING_COLOR));
        statsPanel.add(createStatCard("Bán hôm nay", formatCurrency(todayRevenue), "", SUCCESS_COLOR));
        
        contentWrapper.add(statsPanel);
        contentWrapper.add(Box.createVerticalStrut(25));
        
        // Charts and recent activities
        JPanel chartsRow = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsRow.setBackground(CONTENT_BG);
        
        // Revenue Chart
        JPanel chartCard = createCard("Doanh thu tuần này");
        RevenueChartPanel revenueChart = new RevenueChartPanel();
        chartCard.add(revenueChart, BorderLayout.CENTER);
        
        chartsRow.add(chartCard);
        
        // Recent activities from logs
        JPanel activitiesCard = createCard("Hoạt động gần đây");
        JPanel activitiesContent = new JPanel();
        activitiesContent.setLayout(new BoxLayout(activitiesContent, BoxLayout.Y_AXIS));
        activitiesContent.setBackground(CARD_BG);
        activitiesContent.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        // Load recent logs
        LogBUS logBUS = new LogBUS();
        List<LogDTO> recentLogs = logBUS.getRecentLogs(5);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
        java.util.Date today = new java.util.Date();
        
        for (LogDTO log : recentLogs) {
            Color color = getColorForAction(log.getAction());
            
            // Format time
            String timeStr;
            if (isSameDay(log.getCreatedAt(), today)) {
                timeStr = timeFormat.format(log.getCreatedAt());
            } else {
                timeStr = dateFormat.format(log.getCreatedAt());
            }
            
            // Add username to details
            String displayText = log.getUsername() + ": " + log.getDetails();
            activitiesContent.add(createActivityItem(displayText, timeStr, color));
        }
        
        // Nếu không có logs
        if (recentLogs.isEmpty()) {
            JLabel noDataLabel = new JLabel("Chưa có hoạt động nào");
            noDataLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            noDataLabel.setForeground(TEXT_SECONDARY);
            noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            activitiesContent.add(Box.createVerticalGlue());
            activitiesContent.add(noDataLabel);
            activitiesContent.add(Box.createVerticalGlue());
        }
        
        activitiesCard.add(activitiesContent, BorderLayout.CENTER);
        chartsRow.add(activitiesCard);
        
        contentWrapper.add(chartsRow);
        
        JScrollPane scrollPane = new JScrollPane(contentWrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(CONTENT_BG);
        
        add(scrollPane, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }
    
    private boolean isSameDay(java.util.Date date1, java.util.Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }
    
    private Color getColorForAction(String action) {
        if (action == null) return TEXT_SECONDARY;
        if (action.contains("Đăng nhập") || action.contains("Đăng xuất")) return WARNING_COLOR;
        if (action.contains("Xuất kho") || action.contains("xuất")) return SUCCESS_COLOR;
        if (action.contains("nhập") || action.contains("Nhập")) return PRIMARY_COLOR;
        if (action.contains("Thêm")) return SUCCESS_COLOR;
        if (action.contains("Sửa") || action.contains("Cập nhật")) return DARK_BLUE;
        if (action.contains("Xóa")) return DANGER_COLOR;
        return TEXT_SECONDARY;
    }
    
    private JPanel createStatCard(String title, String value, String subtitle, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR, 1, true), new EmptyBorder(20, 20, 20, 20)));
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        
        // Title with color indicator
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topRow.setOpaque(false);
        
        // Color dot indicator
        JPanel colorDot = new JPanel();
        colorDot.setPreferredSize(new Dimension(8, 8));
        colorDot.setBackground(accentColor);
        colorDot.setOpaque(true);
        
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLbl.setForeground(TEXT_SECONDARY);
        
        topRow.add(colorDot);
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
    
    private JPanel createActivityItem(String text, String time, Color color) {
        JPanel item = new JPanel(new BorderLayout());
        item.setOpaque(false);
        item.setBorder(new EmptyBorder(10, 0, 10, 0));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JPanel leftSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftSection.setOpaque(false);
        
        JPanel colorDot = new JPanel();
        colorDot.setPreferredSize(new Dimension(8, 8));
        colorDot.setBackground(color);
        colorDot.setOpaque(true);
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textLabel.setForeground(TEXT_PRIMARY);
        
        leftSection.add(colorDot);
        leftSection.add(textLabel);
        
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(TEXT_SECONDARY);
        
        item.add(leftSection, BorderLayout.WEST);
        item.add(timeLabel, BorderLayout.EAST);
        
        return item;
    }
    
    private String formatCurrency(double value) {
        if (value >= 1_000_000_000) {
            return String.format("%.1fB₫", value / 1_000_000_000);
        } else if (value >= 1_000_000) {
            return String.format("%.1fM₫", value / 1_000_000);
        } else if (value >= 1_000) {
            return currencyFormat.format(value) + "₫";
        } else if (value == 0) {
            return "0₫";
        } else {
            return currencyFormat.format(value) + "₫";
        }
    }
}
