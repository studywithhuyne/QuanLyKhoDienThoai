package bus;

import java.util.Map;
import dao.StatisticsDAO;

public class StatisticsBUS {
    
    private StatisticsDAO statisticsDAO;
    
    public StatisticsBUS() {
        this.statisticsDAO = new StatisticsDAO();
    }
    
    public double getTotalImport(int month, int year) {
        return statisticsDAO.GetTotalImport(month, year);
    }
    
    public int getImportCount(int month, int year) {
        return statisticsDAO.GetImportCount(month, year);
    }
    
    public int getTodayImportCount() {
        return statisticsDAO.GetTodayImportCount();
    }
    
    public double getTotalSales(int month, int year) {
        return statisticsDAO.GetTotalSales(month, year);
    }
    
    public int getSalesCount(int month, int year) {
        return statisticsDAO.GetSalesCount(month, year);
    }
    
    public double getTodayRevenue() {
        return statisticsDAO.GetTodayRevenue();
    }
    
    public double[] getRevenueLastNDays(int days) {
        return statisticsDAO.GetRevenueLastNDays(days);
    }
    
    public double getTotalInventoryValue() {
        return statisticsDAO.GetTotalInventoryValue();
    }
    
    public int getTotalSkuInStock() {
        return statisticsDAO.GetTotalSkuInStock();
    }
    
    public Map<String, Integer> getStockByCategory() {
        return statisticsDAO.GetStockByCategory();
    }
    
    public Map<String, Integer> getTop5BestSellers(int month, int year) {
        return statisticsDAO.GetTop5BestSellers(month, year);
    }
    
    public Map<String, Integer> getLowStockProducts() {
        return statisticsDAO.GetLowStockProducts();
    }
    
    public double calculateProfitMargin(int month, int year) {
        double totalSales = getTotalSales(month, year);
        double totalImport = getTotalImport(month, year);
        if (totalImport == 0) return 0;
        double profit = totalSales - totalImport;
        return (profit / totalImport) * 100;
    }
}
