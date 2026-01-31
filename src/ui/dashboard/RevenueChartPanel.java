package ui.dashboard;

import static utils.ColorUtil.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

import bus.StatisticsBUS;

/**
 * Panel biểu đồ doanh thu theo ngày trong tuần
 * Sử dụng JPanel thuần để vẽ biểu đồ cột
 */
public class RevenueChartPanel extends JPanel {
    
    private double[] revenueData;
    private String[] dayLabels;
    private double maxRevenue;
    private NumberFormat currencyFormat;
    
    // Chart colors
    private static final Color BAR_COLOR = new Color(99, 102, 241);  
    private static final Color TODAY_COLOR = new Color(34, 197, 94); 
    private static final Color GRID_COLOR = new Color(240, 240, 240);
    
    public RevenueChartPanel() {
        currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        setOpaque(false);
        setBackground(CARD_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 15, 15, 15));
        loadData();
        buildChart();
    }
    
    private final StatisticsBUS statisticsBUS = new StatisticsBUS();
    
    private void loadData() {
        // Lấy dữ liệu 7 ngày gần nhất
        revenueData = statisticsBUS.getRevenueLastNDays(7);
        
        // Tạo labels cho các ngày
        dayLabels = new String[]{"T2", "T3", "T4", "T5", "T6", "T7", "CN"};
        
        // Tìm giá trị max để scale
        maxRevenue = 0;
        for (double value : revenueData) {
            if (value > maxRevenue) maxRevenue = value;
        }
        // Đảm bảo có giá trị min để hiển thị
        if (maxRevenue == 0) maxRevenue = 100000000; // 100 triệu default
    }
    
    private void buildChart() {
        // Main container
        JPanel chartContainer = new JPanel(new BorderLayout(10, 0));
        chartContainer.setOpaque(false);
        
        // Y-axis labels
        JPanel yAxisPanel = new JPanel();
        yAxisPanel.setLayout(new GridLayout(5, 1, 0, 0));
        yAxisPanel.setOpaque(false);
        yAxisPanel.setPreferredSize(new Dimension(65, 0));
        
        for (int i = 4; i >= 0; i--) {
            double value = (maxRevenue / 4) * i;
            JLabel label = new JLabel(formatShortCurrency(value), SwingConstants.RIGHT);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            label.setForeground(TEXT_SECONDARY);
            label.setBorder(new EmptyBorder(0, 0, 0, 8));
            yAxisPanel.add(label);
        }
        
        // Chart area with bars
        JPanel chartArea = new JPanel(new BorderLayout());
        chartArea.setOpaque(false);
        
        // Bars panel
        JPanel barsPanel = new JPanel(new GridLayout(1, 7, 12, 0));
        barsPanel.setOpaque(false);
        barsPanel.setBorder(new EmptyBorder(5, 5, 0, 5));
        
        int todayIndex = java.time.LocalDate.now().getDayOfWeek().getValue() - 1;
        
        for (int i = 0; i < 7; i++) {
            barsPanel.add(createBarColumn(i, i == todayIndex));
        }
        
        chartArea.add(barsPanel, BorderLayout.CENTER);
        
        // Summary panel
        JPanel summaryPanel = createSummaryPanel();
        
        chartContainer.add(yAxisPanel, BorderLayout.WEST);
        chartContainer.add(chartArea, BorderLayout.CENTER);
        
        // Wrapper để thêm summary ở dưới
        JPanel wrapper = new JPanel(new BorderLayout(0, 10));
        wrapper.setOpaque(false);
        wrapper.add(chartContainer, BorderLayout.CENTER);
        wrapper.add(summaryPanel, BorderLayout.SOUTH);
        
        add(wrapper, BorderLayout.CENTER);
    }
    
    private JPanel createBarColumn(int index, boolean isToday) {
        JPanel column = new JPanel(new BorderLayout(0, 5));
        column.setOpaque(false);
        
        // Tính percentage
        double percentage = maxRevenue > 0 ? (revenueData[index] / maxRevenue) : 0;
        
        // Bar container với GridBagLayout để căn bottom
        JPanel barContainer = new JPanel(new GridBagLayout());
        barContainer.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1 - percentage;
        gbc.fill = GridBagConstraints.BOTH;
        
        // Spacer phía trên
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        barContainer.add(spacer, gbc);
        
        // Bar
        gbc.gridy = 1;
        gbc.weighty = percentage > 0 ? percentage : 0.01;
        
        Color barColor = isToday ? TODAY_COLOR : BAR_COLOR;
        
        JPanel bar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Vẽ gradient đơn giản bằng cách thay đổi màu từ trên xuống
                int height = getHeight();
                int width = getWidth();
                
                // Vẽ bar với màu đậm hơn ở dưới
                for (int y = 0; y < height; y++) {
                    float ratio = (float) y / height;
                    int r = (int) (barColor.getRed() * (0.8 + 0.2 * ratio));
                    int gr = (int) (barColor.getGreen() * (0.8 + 0.2 * ratio));
                    int b = (int) (barColor.getBlue() * (0.8 + 0.2 * ratio));
                    g.setColor(new Color(Math.min(r, 255), Math.min(gr, 255), Math.min(b, 255)));
                    g.fillRect(0, y, width, 1);
                }
            }
        };
        bar.setOpaque(false);
        bar.setToolTipText("<html><b>" + dayLabels[index] + "</b><br>" + 
                          currencyFormat.format(revenueData[index]) + "₫</html>");
        
        // Border với bo góc trên
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 0, 1, barColor.darker()),
            BorderFactory.createEmptyBorder(3, 0, 0, 0)
        ));
        
        barContainer.add(bar, gbc);
        
        // Day label
        JPanel dayLabelPanel = new JPanel(new BorderLayout());
        dayLabelPanel.setOpaque(false);
        dayLabelPanel.setPreferredSize(new Dimension(0, 25));
        
        JLabel dayLabel = new JLabel(dayLabels[index], SwingConstants.CENTER);
        dayLabel.setFont(new Font("Segoe UI", isToday ? Font.BOLD : Font.PLAIN, 11));
        dayLabel.setForeground(isToday ? TODAY_COLOR : TEXT_SECONDARY_DARK);
        dayLabelPanel.add(dayLabel, BorderLayout.CENTER);
        
        // Dot indicator cho ngày hôm nay
        if (isToday) {
            JPanel dotPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 2));
            dotPanel.setOpaque(false);
            
            JPanel dot = new JPanel() {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(6, 6);
                }
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(TODAY_COLOR);
                    g.fillOval(0, 0, 6, 6);
                }
            };
            dot.setOpaque(false);
            dotPanel.add(dot);
            
            dayLabelPanel.add(dotPanel, BorderLayout.SOUTH);
            dayLabelPanel.setPreferredSize(new Dimension(0, 30));
        }
        
        column.add(barContainer, BorderLayout.CENTER);
        column.add(dayLabelPanel, BorderLayout.SOUTH);
        
        return column;
    }
    
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setOpaque(false);
        panel.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, BORDER_COLOR),
            new EmptyBorder(12, 10, 5, 10)
        ));
        
        // Tổng doanh thu tuần
        double totalRevenue = 0;
        for (double v : revenueData) totalRevenue += v;
        
        panel.add(createSummaryItem("Tổng tuần", totalRevenue, DARK_BLUE));
        
        // Trung bình ngày
        double avgRevenue = totalRevenue / 7;
        panel.add(createSummaryItem("TB/ngày", avgRevenue, TEXT_SECONDARY_DARK));
        
        // Hôm nay
        int todayIdx = java.time.LocalDate.now().getDayOfWeek().getValue() - 1;
        panel.add(createSummaryItem("Hôm nay", revenueData[todayIdx], TODAY_COLOR));
        
        return panel;
    }
    
    private JPanel createSummaryItem(String label, double value, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        JLabel lblTitle = new JLabel(label);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTitle.setForeground(TEXT_SECONDARY);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblValue = new JLabel(formatShortCurrency(value));
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblValue.setForeground(color);
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblValue);
        
        return panel;
    }
    
    private String formatShortCurrency(double value) {
        if (value >= 1_000_000_000) {
            return String.format("%.1fB", value / 1_000_000_000);
        } else if (value >= 1_000_000) {
            return String.format("%.1fM", value / 1_000_000);
        } else if (value >= 1_000) {
            return String.format("%.0fK", value / 1_000);
        } else if (value == 0) {
            return "0";
        } else {
            return String.format("%.0f", value);
        }
    }
    
    /**
     * Refresh data và rebuild chart
     */
    public void refreshData() {
        removeAll();
        loadData();
        buildChart();
        revalidate();
        repaint();
    }
}
