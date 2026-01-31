package ui.statistics;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import static utils.ColorUtil.*;
import bus.StatisticsBUS;

import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Panel thống kê - Thống kê nhập hàng, xuất hàng, tồn kho
 */
public class StatisticsPanel extends JPanel {
    
    private JFrame parentFrame;
    private StatisticsBUS statisticsBUS;
    private NumberFormat currencyFormat;
    
    // Stat cards labels
    private JLabel lblImportValue;
    private JLabel lblImportCount;
    private JLabel lblSalesValue;
    private JLabel lblSalesCount;
    private JLabel lblInventoryValue;
    
    // Filter components
    private JComboBox<String> cboMonth;
    private JComboBox<Integer> cboYear;
    private JLabel lblImportTitle;
    private JLabel lblSalesTitle;
    
    public StatisticsPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.statisticsBUS = new StatisticsBUS();
        this.currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        initializePanel();
        loadData();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(CONTENT_BG);
        setBorder(new EmptyBorder(25, 30, 25, 30));
        
        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setBackground(CONTENT_BG);
        
        // Header with filter and refresh button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CONTENT_BG);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JLabel titleLabel = new JLabel("Thống kê tổng quan");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setBackground(CONTENT_BG);
        
        JLabel lblFilter = new JLabel("Lọc theo:");
        lblFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Month combo
        String[] months = {"Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
                          "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"};
        cboMonth = new JComboBox<>(months);
        cboMonth.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboMonth.setPreferredSize(new Dimension(100, 30));
        cboMonth.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        cboMonth.addActionListener(e -> loadData());
        
        // Year combo
        int currentYear = LocalDate.now().getYear();
        Integer[] years = new Integer[5];
        for (int i = 0; i < 5; i++) {
            years[i] = currentYear - i;
        }
        cboYear = new JComboBox<>(years);
        cboYear.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboYear.setPreferredSize(new Dimension(80, 30));
        cboYear.addActionListener(e -> loadData());
        
        filterPanel.add(lblFilter);
        filterPanel.add(cboMonth);
        filterPanel.add(cboYear);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(filterPanel, BorderLayout.EAST);
        
        contentWrapper.add(headerPanel);
        contentWrapper.add(Box.createVerticalStrut(20));
        
        // Stats cards row
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(CONTENT_BG);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        
        // Import card
        JPanel importCard = createStatCard("Tổng nhập", PRIMARY_COLOR);
        JPanel importContent = (JPanel)importCard.getComponent(0);
        lblImportTitle = (JLabel) importContent.getComponent(0);
        lblImportValue = (JLabel) importContent.getComponent(1);
        lblImportCount = (JLabel) importContent.getComponent(2);
        statsPanel.add(importCard);
        
        // Sales card  
        JPanel salesCard = createStatCard("Tổng xuất", SUCCESS_COLOR);
        JPanel salesContent = (JPanel)salesCard.getComponent(0);
        lblSalesTitle = (JLabel) salesContent.getComponent(0);
        lblSalesValue = (JLabel) salesContent.getComponent(1);
        lblSalesCount = (JLabel) salesContent.getComponent(2);
        statsPanel.add(salesCard);
        
        // Inventory card
        JPanel inventoryCard = createStatCard("Giá trị tồn kho", WARNING_COLOR);
        lblInventoryValue = (JLabel) ((JPanel)inventoryCard.getComponent(0)).getComponent(1);
        statsPanel.add(inventoryCard);
        
        contentWrapper.add(statsPanel);
        contentWrapper.add(Box.createVerticalStrut(25));
        
        // Tables row
        JPanel tablesRow = new JPanel(new GridLayout(1, 2, 20, 0));
        tablesRow.setBackground(CONTENT_BG);
        tablesRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
        
        // Top sellers table
        tablesRow.add(createTopSellersCard());
        
        // Low stock table
        tablesRow.add(createLowStockCard());
        
        contentWrapper.add(tablesRow);
        contentWrapper.add(Box.createVerticalStrut(25));
        
        // Stock by category
        contentWrapper.add(createStockByCategoryCard());
        
        JScrollPane scrollPane = new JScrollPane(contentWrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(CONTENT_BG);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadData() {
        // Get selected month/year
        int month = cboMonth.getSelectedIndex() + 1;
        int year = (Integer) cboYear.getSelectedItem();
        String monthYearText = "tháng " + month + "/" + year;
        
        // Load summary stats with filter
        double importTotal = statisticsBUS.getTotalImport(month, year);
        int importCount = statisticsBUS.getImportCount(month, year);
        double salesTotal = statisticsBUS.getTotalSales(month, year);
        int salesCount = statisticsBUS.getSalesCount(month, year);
        double inventoryValue = statisticsBUS.getTotalInventoryValue();
        
        // Update titles
        lblImportTitle.setText("Tổng nhập " + monthYearText);
        lblSalesTitle.setText("Tổng xuất " + monthYearText);
        
        // Update labels
        lblImportValue.setText(formatCurrency(importTotal));
        lblImportCount.setText(importCount + " phiếu nhập");
        lblSalesValue.setText(formatCurrency(salesTotal));
        lblSalesCount.setText(salesCount + " phiếu xuất");
        lblInventoryValue.setText(formatCurrency(inventoryValue));
        
        // Refresh tables with filter
        refreshTopSellersTable(month, year);
        refreshLowStockTable();
        refreshStockByCategoryTable();
    }
    
    private String formatCurrency(double value) {
        return currencyFormat.format(value) + "₫";
    }
    
    private JPanel createStatCard(String title, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR, 1, true), new EmptyBorder(20, 20, 20, 20)));
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        
        // Title
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLbl.setForeground(TEXT_SECONDARY);
        titleLbl.setBorder(new EmptyBorder(0, 0, 10, 0));
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Value
        JLabel valueLabel = new JLabel("0₫");
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(TEXT_PRIMARY);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Count (for import/sales cards)
        JLabel countLabel = new JLabel("");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        countLabel.setForeground(TEXT_SECONDARY);
        countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        countLabel.setBorder(new EmptyBorder(5, 0, 0, 0));
        
        content.add(titleLbl);
        content.add(valueLabel);
        content.add(countLabel);
        
        card.add(content, BorderLayout.CENTER);
        
        return card;
    }
    
    private JTable topSellersTable;
    private DefaultTableModel topSellersModel;
    private JLabel lblTopSellersTitle;

    private JPanel createTopSellersCard() {
        JPanel card = createCardWithDynamicTitle("Top sản phẩm bán chạy");
        lblTopSellersTitle = (JLabel)((JPanel)card.getComponent(0)).getComponent(0);
        
        String[] columns = {"#", "Sản phẩm", "SL bán"};
        topSellersModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        
        topSellersTable = new JTable(topSellersModel);
        topSellersTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        topSellersTable.setRowHeight(35);
        topSellersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        topSellersTable.getTableHeader().setBackground(CONTENT_BG);
        topSellersTable.setShowGrid(false);
        topSellersTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Column widths
        topSellersTable.getColumnModel().getColumn(0).setMaxWidth(40);
        topSellersTable.getColumnModel().getColumn(2).setMaxWidth(80);
        
        JScrollPane scroll = new JScrollPane(topSellersTable);
        scroll.setBorder(null);
        
        card.add(scroll, BorderLayout.CENTER);
        
        return card;
    }
    
    private void refreshTopSellersTable(int month, int year) {
        topSellersModel.setRowCount(0);
        lblTopSellersTitle.setText("Top sản phẩm bán chạy tháng " + month + "/" + year);
        java.util.Map<String, Integer> data = statisticsBUS.getTop5BestSellers(month, year);
        int rank = 1;
        for (java.util.Map.Entry<String, Integer> entry : data.entrySet()) {
            topSellersModel.addRow(new Object[]{rank++, entry.getKey(), entry.getValue()});
        }
        if (data.isEmpty()) {
            topSellersModel.addRow(new Object[]{"", "Chưa có dữ liệu tháng này", ""});
        }
    }
    
    private JTable lowStockTable;
    private DefaultTableModel lowStockModel;
    
    private JPanel createLowStockCard() {
        JPanel card = createCard("Sản phẩm sắp hết hàng");
        
        String[] columns = {"Sản phẩm", "Tồn"};
        lowStockModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        
        lowStockTable = new JTable(lowStockModel);
        lowStockTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lowStockTable.setRowHeight(35);
        lowStockTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        lowStockTable.getTableHeader().setBackground(CONTENT_BG);
        lowStockTable.setShowGrid(false);
        lowStockTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Column widths
        lowStockTable.getColumnModel().getColumn(1).setMaxWidth(60);
        
        // Highlight low stock in red
        lowStockTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (col == 1 && value instanceof Integer) {
                    int stock = (Integer) value;
                    if (stock <= 2) {
                        c.setForeground(DANGER_COLOR);
                        ((JLabel)c).setFont(new Font("Segoe UI", Font.BOLD, 13));
                    } else {
                        c.setForeground(WARNING_COLOR);
                    }
                } else {
                    c.setForeground(TEXT_PRIMARY);
                }
                return c;
            }
        });
        
        JScrollPane scroll = new JScrollPane(lowStockTable);
        scroll.setBorder(null);
        
        card.add(scroll, BorderLayout.CENTER);
        
        return card;
    }
    
    private void refreshLowStockTable() {
        lowStockModel.setRowCount(0);
        java.util.Map<String, Integer> data = statisticsBUS.getLowStockProducts();
        for (java.util.Map.Entry<String, Integer> entry : data.entrySet()) {
            lowStockModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
        if (data.isEmpty()) {
            lowStockModel.addRow(new Object[]{"Không có sản phẩm sắp hết", ""});
        }
    }
    
    private JTable stockByCategoryTable;
    private DefaultTableModel stockByCategoryModel;
    
    private JPanel createStockByCategoryCard() {
        JPanel card = createCard("Tồn kho theo danh mục");
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        
        String[] columns = {"Danh mục", "Số lượng"};
        stockByCategoryModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        
        stockByCategoryTable = new JTable(stockByCategoryModel);
        stockByCategoryTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        stockByCategoryTable.setRowHeight(40);
        stockByCategoryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        stockByCategoryTable.getTableHeader().setBackground(CONTENT_BG);
        stockByCategoryTable.setShowGrid(false);
        stockByCategoryTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Column widths
        stockByCategoryTable.getColumnModel().getColumn(0).setPreferredWidth(300);
        stockByCategoryTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        
        // Center align for quantity column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        stockByCategoryTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        
        JScrollPane scroll = new JScrollPane(stockByCategoryTable);
        scroll.setBorder(null);
        
        card.add(scroll, BorderLayout.CENTER);
        
        return card;
    }
    
    private void refreshStockByCategoryTable() {
        stockByCategoryModel.setRowCount(0);
        Map<String, Integer> data = statisticsBUS.getStockByCategory();
        
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            stockByCategoryModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
        
        if (data.isEmpty()) {
            stockByCategoryModel.addRow(new Object[]{"Chưa có dữ liệu", ""});
        }
    }
    
    private JPanel createCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR), new EmptyBorder(12, 15, 12, 15)));
        
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setForeground(TEXT_PRIMARY);
        header.add(titleLbl, BorderLayout.WEST);
        
        card.add(header, BorderLayout.NORTH);
        
        return card;
    }
    
    private JPanel createCardWithDynamicTitle(String title) {
        return createCard(title);
    }
}
